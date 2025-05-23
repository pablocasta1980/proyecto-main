package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.AutenticacionServicio;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutenticacionServicioImpl  implements AutenticacionServicio {


    private final UsuarioRepo usuarioRepo;
    private final EmailServicio emailServicio;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    public TokenDTO login(LoginDTO loginDTO) throws Exception {


        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(loginDTO.email());


        if(optionalUsuario.isEmpty()){
            throw new Exception("El usuario no existe");
        }

        Usuario usuario = optionalUsuario.get();

        // Validar si el usuario está activo
        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new Exception("El usuario no está activo. Verifique su cuenta.");
        }

        // Verificar si la contraseña es correcta usando el PasswordEncoder
        if(!passwordEncoder.matches(loginDTO.password(), usuario.getPassword())){
            throw new Exception("El usuario no existe");
        }


        String token = jwtUtils.generateToken(usuario.getId().toString(), crearClaims(usuario), 500);
        return new TokenDTO(token);
    }


    private Map<String, String> crearClaims(Usuario usuario){
        return Map.of(
                "email", usuario.getEmail(),
                "nombre", usuario.getNombre(),
                "rol", "ROLE_"+usuario.getRol().name()
        );
    }



    @Override
    public boolean recuperarPassword(RecuperarPasswordDTO recuperarPasswordDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(recuperarPasswordDTO.email());

        if (usuarioOptional.isEmpty()) {
            return false; // No se encontró el usuario
        }

        Usuario usuario = usuarioOptional.get();

        // Generar el token de recuperación usando JWT con claims personalizados
        Map<String, String> claims = Map.of(
                "rol", usuario.getRol().name(), // Usa el rol real del usuario
                "tipo", "recuperacion"
        );

        // Puedes mover esto a un servicio aparte si deseas centralizar
        String tokenRecuperacion = jwtUtils.generateToken(usuario.getEmail(), claims, 15); // 15 minutos

        // Crear el contenido del correo
        String cuerpoCorreo = "Copia el siguiente código para recuperar tu cuenta:\n\n"
                + tokenRecuperacion + "\n\n"
                + "Token expira en 15 minutos.";

        EmailDTO emailDTO = new EmailDTO(
                usuario.getEmail(),
                "Recuperación de Contraseña",
                cuerpoCorreo
        );

        try {
            emailServicio.enviarCorreo(emailDTO);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }



    @Override
    public String cambiarPasswordConToken(CambioPasswordConTokenDTO dto) throws Exception{

        if (!jwtUtils.isTokenValid(dto.token())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
        }

        String emailToken = jwtUtils.getUsernameFromToken(dto.token());
        String tipo = jwtUtils.getClaim(dto.token(), "tipo");

        if (!"recuperacion".equals(tipo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El token no es de recuperación");
        }

        if (!emailToken.equals(dto.email())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no coincide con el token");
        }

        Usuario usuario = usuarioRepo.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(dto.nuevaPassword()));
        usuarioRepo.save(usuario);

        return "Contraseña actualizada exitosamente";
    }


}



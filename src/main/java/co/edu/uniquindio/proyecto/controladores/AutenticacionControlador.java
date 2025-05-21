package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.LoginDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.AutenticacionServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import co.edu.uniquindio.proyecto.dto.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})

public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;
    private final JWTUtils jwtUtils;
    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        TokenDTO tokenDTO = autenticacionServicio.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/recuperarPassword")
    public ResponseEntity<MensajeDTO<String>> recuperarPassword(@Valid @RequestBody RecuperarPasswordDTO recuperarPasswordDTO) throws Exception {
        boolean exito = autenticacionServicio.recuperarPassword(recuperarPasswordDTO);

        if (exito) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo de recuperación enviado", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensajeDTO<>(true, "No se pudo enviar el correo de recuperación", null));        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPasswordConToken(@RequestBody CambioPasswordConTokenDTO dto)throws Exception {
        String respuesta = autenticacionServicio.cambiarPasswordConToken(dto);
        return ResponseEntity.ok(respuesta);
    }



}
package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.*;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepo usuarioRepo;
    private final UsuarioMapper usuarioMapper;
    private final MongoTemplate mongoTemplate;
    private final EmailServicio emailServicio;
    private final PasswordEncoder passwordEncoder; // Inyecta el encoder
    private final JWTUtils jwtUtils;



    private boolean existeEmail(String email) {
        return usuarioRepo.findByEmail(email).isPresent();
    }

    @Override
    public void crear(CrearUsuarioDTO crearUsuarioDTO) throws Exception {

        if (existeEmail(crearUsuarioDTO.email())) {
            throw new ElementoRepetidoException("El email ya está registrado");
        }

        Usuario usuario = usuarioMapper.toDocument(crearUsuarioDTO);
        usuario.setPassword(passwordEncoder.encode(crearUsuarioDTO.password()));
        usuario.setEstado(EstadoUsuario.INACTIVO);
        usuario.setFechaRegistro(LocalDateTime.now());

        // Generar token único de activación
        GenerarTokenDTO token = generarToken(crearUsuarioDTO.email());
        usuario.setTokenActivacion(token.token());
        usuarioRepo.save(usuario);

        // Crear el correo con el token
        EmailDTO emailDTO = new EmailDTO(
                crearUsuarioDTO.email(),
                "Token de Activación - Alertas Urbanas",
                "Hola, gracias por registrarte en Alertas Urbanas.\n\n" +
                        "Para activar tu cuenta, utiliza el siguiente token de autenticación:\n\n" +
                        token.token() + "\n\n" +
                        "Este token expirará en 15 minutos."
        );

        // Enviar el correo
        emailServicio.enviarCorreo(emailDTO);
    }

    @Override
    public boolean activarCuenta(String email, String codigo) {
        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Verificar si ya está activo
            if (usuario.getEstado() == EstadoUsuario.ACTIVO) {
                return false; // La cuenta ya está activa
            }

            try {
                // Validar el token JWT
                if (usuario.getTokenActivacion() != null) {
                    Claims claims = jwtUtils.extractAllClaims(usuario.getTokenActivacion());

                    // Verificar que el token no haya expirado
                    if (claims.getExpiration().before(new Date())) {
                        throw new TokenExpiradoException("El token de activación ha expirado.");
                    }


                    // Comparar el token con el que llegó
                    if (usuario.getTokenActivacion().equals(codigo)) {
                        usuario.setEstado(EstadoUsuario.ACTIVO);
                        usuario.setTokenActivacion(null); // Eliminar el código usado
                        usuarioRepo.save(usuario);
                        return true;
                    }
                }

            }  catch (ExpiredJwtException e) {
            throw new TokenExpiradoException("El token ha expirado.");
        } catch (Exception e) {
            throw new TokenInvalidoException("El token de activación es inválido.");
        }
        }

        return false;
    }


    @Override
    public void eliminar(String id) throws Exception {
        ObjectId objectId = new ObjectId(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        // Verificar que el usuario autenticado solo pueda eliminar su propia cuenta
        if (!id.equals(idUsuarioAutenticado)) {
            throw new AccesoNoAutorizadoException("No puedes eliminar la cuenta de otro usuario.");
        }


        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepo.findById(objectId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un usuario con el ID: " + id));

        // Eliminar lógicamente el usuario (cambiando su estado en lugar de eliminar físicamente)
        usuario.setEstado(EstadoUsuario.ELIMINADO); // Asegúrate de que `EstadoUsuario` tenga un valor `INACTIVO`
        usuarioRepo.save(usuario);
    }




    @Override
    public void editarCuenta(String id, EditarUsuarioDTO editarUsuarioDTO) throws Exception {
        ObjectId objectId = new ObjectId(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        // Verificar que el usuario autenticado solo pueda modificar su propia cuenta
        if (!id.equals(idUsuarioAutenticado)) {
            throw new AccesoNoAutorizadoException("No puedes editar la cuenta de otro usuario.");
        }


        // Buscar el usuario en la base de datos
        Usuario cuentaModificada = usuarioRepo.findById(objectId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un usuario con el ID: " + id));

        // Actualizar solo los campos necesarios si se envían en el request
        if (editarUsuarioDTO.ciudad() != null) {
            cuentaModificada.setCiudad(Ciudad.valueOf(editarUsuarioDTO.ciudad().toUpperCase()));
        }
        if (editarUsuarioDTO.direccion() != null) {
            cuentaModificada.setDireccion(editarUsuarioDTO.direccion());
        }
        if (editarUsuarioDTO.telefono() != null) {
            cuentaModificada.setTelefono(editarUsuarioDTO.telefono());
        }

        // Guardar cambios en la base de datos
        usuarioRepo.save(cuentaModificada);
    }





    @Override
    public UsuarioDTO obtener(String id) throws RecursoNoEncontradoException {
        Usuario usuario = obtenerPorId(id);

        return usuarioMapper.toDTO(usuario);
    }


    @Override
    public List<UsuarioDTO> listarTodos(String nombre, String ciudad, int pagina) {
        if (pagina < 0) {
            throw new ParametroInvalidoException("La página no puede ser menor a 0");
        }

        Query query = new Query();

        // Filtros opcionales
        if (nombre != null && !nombre.isBlank()) {
            query.addCriteria(Criteria.where("nombre").regex(nombre, "i"));
        }

        if (ciudad != null && !ciudad.isBlank()) {
            query.addCriteria(Criteria.where("ciudad").regex(ciudad, "i"));
        }

        // Solo rol CLIENTE
        query.addCriteria(Criteria.where("rol").is(Rol.CLIENTE.name()));

        // Paginación
        query.with(PageRequest.of(pagina, 5));

        // Consulta
        List<Usuario> usuarios = mongoTemplate.find(query, Usuario.class);

        // Mapear a DTO
        return usuarios.stream().map(usuarioMapper::toDTO).toList();
    }

   /* public Usuario obtenerPorId(String id) throws RecursoNoEncontradoException {
        // Buscamos el usuario que se quiere obtener
//        if (!ObjectId.isValid(id)) {
//            throw new RecursoNoEncontradoException("No se encontró el usuario con el id "+id);
//        }

        // Convertimos el id de String a ObjectId
//        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> optionalCuenta = usuarioRepo.findByEmail(id);

        // Si no se encontró el usuario, lanzamos una excepción
        if(optionalCuenta.isEmpty()){
            throw new RecursoNoEncontradoException("No se encontró el usuario con el id "+id);
        }

        return optionalCuenta.get();
    }*/
   public Usuario obtenerPorId(String id) throws RecursoNoEncontradoException {
       Optional<Usuario> optionalCuenta = usuarioRepo.findById(id); // CORREGIDO

       if(optionalCuenta.isEmpty()){
           throw new RecursoNoEncontradoException("No se encontró el usuario con el id " + id);
       }

       return optionalCuenta.get();
   }



    @Override
    public boolean cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        // Buscar el usuario por su email
        Usuario usuario = usuarioRepo.findByEmail(cambiarPasswordDTO.email())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con ese email"));

        if (!passwordEncoder.matches(cambiarPasswordDTO.passwordActual(), usuario.getPassword())) {
            throw new ValidacionException("La contraseña actual es incorrecta");
        }


        // Encriptar la nueva contraseña antes de guardarla
        String nuevaPasswordEncriptada = passwordEncoder.encode(cambiarPasswordDTO.nuevaPassword());
        usuario.setPassword(nuevaPasswordEncriptada);
        usuarioRepo.save(usuario);

        return true;
    }


    @Override
    public GenerarTokenDTO generarToken(String email) {
        Map<String, String> claims = Map.of(
                "rol", Rol.CLIENTE.name(),
                "tipo", "activacion"
        );

        String token = jwtUtils.generateToken(email, claims, 15);

        Instant expiracion = Instant.now().plus(15, ChronoUnit.MINUTES);

        return new GenerarTokenDTO(token, expiracion);
    }


    @Override
    public void reenviarToken(String email) throws Exception {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ese email"));

        if (usuario.getEstado() == EstadoUsuario.ACTIVO) {
            throw new AccionDuplicadaException("La cuenta ya fue activada");
        }


        GenerarTokenDTO nuevoToken = generarToken(email);
        usuario.setTokenActivacion(nuevoToken.token());
        usuarioRepo.save(usuario);

        EmailDTO emailDTO = new EmailDTO(
                email,
                "Nuevo Token de Activación - Alertas Urbanas",
                "Hola, aquí tienes un nuevo token de activación:\n\n" + nuevoToken.token() +
                        "\n\nEste token expirará en 15 minutos."
        );

        emailServicio.enviarCorreo(emailDTO);
    }



    @Override
    public void actualizarSuscripcionNotificaciones(String idUsuario, SuscripcionNotificacionesDTO suscripcion) throws Exception {
        Usuario usuario = obtenerPorId(idUsuario);

        usuario.setNotificacionApp(suscripcion.notificacion_app());
        usuario.setNotificacionEmail(suscripcion.notificacion_email());

        usuarioRepo.save(usuario);
    }

}
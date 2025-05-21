package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})

public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private final JWTUtils jwtUtils;
    private final UsuarioRepo usuarioRepo;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/registro")
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CrearUsuarioDTO cuenta) throws Exception {
        usuarioServicio.crear(cuenta);
        return ResponseEntity.status(201).body(
                new MensajeDTO<>(false, "Pre-registro exitoso. Revisa el token enviado a tu correo para activar tu cuenta.", null)
        );
    }

    @PutMapping("/activar/{email}/{codigo}")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@PathVariable String email, @PathVariable String codigo) {
        boolean activado = usuarioServicio.activarCuenta(email, codigo);

        if (activado) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta activada correctamente", null));
        } else {
            return ResponseEntity.status(400).body(new MensajeDTO<>(true, "Código de activación incorrecto", null));
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception {

        // Llamar al servicio para eliminar la cuenta
        usuarioServicio.eliminar(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente", null));

    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable String id) throws Exception {
        Usuario usuario = usuarioServicio.obtenerPorId(id);

        /*if (usuario == null) {
            return ResponseEntity.status(400).body(new MensajeDTO<>(true, "Usuario no encontrado", null));
        }*/
        UsuarioDTO info = usuarioMapper.toDTO(usuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lista de usuarios", info));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<MensajeDTO<List<UsuarioDTO>>> listarTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam(defaultValue = "0") int pagina // default para evitar errores
    ) {
        List<UsuarioDTO> lista = usuarioServicio.listarTodos(nombre, ciudad, pagina);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lista de usuarios", lista));
    }



    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@PathVariable String id,
                                                           @Valid @RequestBody EditarUsuarioDTO cuenta) throws Exception {

        // Llamar al servicio para editar la cuenta
        usuarioServicio.editarCuenta(id, cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente", null));
    }


    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/actualizar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        usuarioServicio.cambiarPassword(cambiarPasswordDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña cambiada exitosamente", null));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/notificaciones/suscribirse")
    public ResponseEntity<MensajeDTO<String>> suscribirseNotificaciones(
            @RequestParam String idUsuario,
            @Valid @RequestBody SuscripcionNotificacionesDTO suscripcion) throws Exception {

        usuarioServicio.actualizarSuscripcionNotificaciones(idUsuario, suscripcion);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Preferencias de notificación actualizadas correctamente", null));
    }


    @PostMapping("/reenviar-token")
    public ResponseEntity<MensajeDTO<String>> reenviarToken(@RequestParam String email) throws Exception {
            usuarioServicio.reenviarToken(email);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Se ha enviado un nuevo token a tu correo", null));
    }


}
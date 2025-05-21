package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.servicios.ComentarioServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioControlador {

    private final ComentarioServicio comentarioServicio;


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{idReporte}")
    public ResponseEntity<?> crearComentario(@PathVariable String idReporte,
                                             @RequestBody CrearComentarioDTO dto) throws Exception{
        Comentario comentario = comentarioServicio.crearComentario(idReporte, dto);
        return ResponseEntity.ok(comentario);
    }

    @GetMapping("/{idReporte}")
    public ResponseEntity<MensajeDTO> obtenerComentariosPorReporte(@PathVariable String idReporte) {
        try {
            List<Comentario> comentarios = comentarioServicio.obtenerComentariosPorIdReporte(idReporte);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Comentarios del reporte obtenidos", comentarios));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MensajeDTO<>(true, e.getMessage(), null));
        }
    }


}

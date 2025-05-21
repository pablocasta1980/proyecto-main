package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.ActualizarCategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CrearCategoriaDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorias")
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<MensajeDTO<CategoriaDTO>> crear(@Valid @RequestBody CrearCategoriaDTO dto) throws Exception {
        CategoriaDTO guardada = categoriaServicio.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MensajeDTO<>(false, "Categoría creada exitosamente", guardada));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizar(@PathVariable String id, @Valid @RequestBody ActualizarCategoriaDTO categoria) throws Exception {
        categoriaServicio.actualizarCategoria(id, categoria);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría actualizada exitosamente", null));
    }


    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception {
        categoriaServicio.eliminarCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría eliminada exitosamente", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<CategoriaDTO>> obtener(@PathVariable String id) throws Exception {
        CategoriaDTO info = categoriaServicio.obtenerCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría obtenida correctamente", info));
    }

    @GetMapping
    public ResponseEntity<MensajeDTO<List<CategoriaDTO>>> listar() throws Exception {
        List<CategoriaDTO> lista = categoriaServicio.listarCategorias();
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lista de categorías obtenida con éxito", lista));
    }
}
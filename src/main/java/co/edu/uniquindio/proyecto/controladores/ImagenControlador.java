package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.ImagesServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/imagenes")
public class ImagenControlador {

    private final ImagesServicio imagesServicio;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MensajeDTO<Map>> subirImagen(MultipartFile archivo) throws Exception {
        try {
            Map datos = imagesServicio.subirImagen(archivo);
            return ResponseEntity.status(200)
                    .body(new MensajeDTO<>(false, "Imagen subida exitosamente.", datos));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new MensajeDTO<>(true, "Error al subir imagen: " + e.getMessage(), null));
        }
    }


}

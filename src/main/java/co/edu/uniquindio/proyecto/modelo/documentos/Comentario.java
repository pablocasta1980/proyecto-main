package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("comentario")
public class Comentario {

    @Id
    private String id;

    private String comentarioTexto;

    private LocalDateTime fecha;

    private String usuarioId;  // Asegúrate de que estos campos existan

    private String reporteId;  // Asegúrate de que estos campos existan
}


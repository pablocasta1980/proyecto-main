package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("notificaciones")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    private String id;

    private String idUsuario; // A quién va dirigida la notificación
    private String mensaje;
    private String cuerpo;
    private String topic;

    private LocalDateTime fecha;
    private boolean leida;
}

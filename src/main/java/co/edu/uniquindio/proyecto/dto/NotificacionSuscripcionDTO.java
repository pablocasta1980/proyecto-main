package co.edu.uniquindio.proyecto.dto;

import lombok.Data;

@Data
public class NotificacionSuscripcionDTO {
    private String idUsuario;
    private boolean notificacionApp;
    private boolean notificacionEmail;
}

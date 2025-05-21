package co.edu.uniquindio.proyecto.dto;

public record MensajeDTO<T>(boolean error, String mensaje, T data) {
}


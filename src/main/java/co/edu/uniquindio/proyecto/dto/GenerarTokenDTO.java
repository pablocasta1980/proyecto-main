package co.edu.uniquindio.proyecto.dto;

import java.time.Instant;

public record GenerarTokenDTO(String token, Instant expiracion) {
}

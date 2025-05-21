package co.edu.uniquindio.proyecto.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CalificacionDTO(
        @Min(value = 1, message = "La calificación mínima es 1")
        @Max(value = 5, message = "La calificación máxima es 5")
        int estrellas
) {}


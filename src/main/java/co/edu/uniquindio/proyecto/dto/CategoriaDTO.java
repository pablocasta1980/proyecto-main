package co.edu.uniquindio.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        @NotNull String id,
        @NotNull String nombre,
        @NotBlank String descripcion
) {
}

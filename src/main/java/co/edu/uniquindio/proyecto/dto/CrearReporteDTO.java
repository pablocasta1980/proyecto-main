package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record CrearReporteDTO(
        @NotBlank @Length(max = 150) String titulo,
        @NotBlank @Length(max = 400) String descripcion,
        @NotEmpty String rutaImagenes,
        @NotBlank String categoria,
        @NotNull UbicacionDTO ubicacionDTO,
        @NotBlank String idUsuario,
        @NotNull LocalDateTime fechaCreacion,
        @NotNull EstadoReporte estado
) {
}
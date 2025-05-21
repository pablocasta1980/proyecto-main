package co.edu.uniquindio.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CambiarPasswordDTO(
        @NotBlank String email,
        @NotBlank @Length(min = 7, max = 20) String passwordActual,
        @NotBlank @Length(min = 7, max = 20) String nuevaPassword
) { }

package co.edu.uniquindio.proyecto.dto;

import java.time.LocalDate;

public record BuscarReporteDTO(
        String txtConsulta,
        String categoria,
        String estado,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Double latitud,
        Double longitud
) {}

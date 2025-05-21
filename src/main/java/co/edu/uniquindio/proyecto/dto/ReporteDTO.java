package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ReporteDTO(
        String id,
        String nombre,
        String descripcion,
        String categoria,
        LocalDateTime fechaCreacion,
        EstadoReporte estado,
        UbicacionDTO ubicacionDTO,
        int conteoImportantes,
        double promedioEstrellas,
        Map<String, Integer> calificaciones

) {}


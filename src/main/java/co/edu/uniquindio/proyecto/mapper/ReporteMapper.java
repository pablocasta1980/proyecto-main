package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.ReporteDTO;
import co.edu.uniquindio.proyecto.dto.UbicacionDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import org.mapstruct.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    Reporte toDocument(CrearReporteDTO crearReporteDTO);

    void toDocument(EditarReporteDTO editarReporteDTO, @MappingTarget Reporte reporte);

    default ReporteDTO toDTO(Reporte reporte) {
        if (reporte == null) return null;

        return new ReporteDTO(
                reporte.getId(),
                reporte.getNombre(),
                reporte.getDescripcion(),
                reporte.getCategoria(),
                reporte.getFechaCreacion(),
                reporte.getEstado(),
                geoJsonPointToUbicacionDTO(reporte.getUbicacion()),
                reporte.getConteoImportante(),
                reporte.getPromedioEstrellas(),     // <-- Nuevo
                reporte.getCalificaciones()         // <-- Nuevo
        );
    }

    default UbicacionDTO geoJsonPointToUbicacionDTO(GeoJsonPoint point) {
        if (point == null) return null;
        return new UbicacionDTO(point.getY(), point.getX()); // Y = latitud, X = longitud
    }
}

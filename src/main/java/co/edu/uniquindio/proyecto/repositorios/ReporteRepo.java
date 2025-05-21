package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepo extends MongoRepository<Reporte, String> {

    // Obtener reportes con paginación
    Page<Reporte> findAll(Pageable pageable);

    // Buscar reportes por categoría con paginación
    Page<Reporte> findByCategoria(String categoria, Pageable pageable);

    // Buscar reportes de un usuario por su ID, ordenados por fecha
    List<Reporte> findByUsuarioIdOrderByFechaCreacionAsc(String usuarioId);
    List<Reporte> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId);

    // Buscar reportes cuyo nombre contenga un texto
    @Query("{'nombre': {$regex: ?0, $options: 'i'}}")
    Page<Reporte> buscarPorNombre(String texto, Pageable pageable);

    @Query("{ 'ubicacion': { $near: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: 5000 } } }")
    Page<Reporte> obtenerCercanos(double longitud, double latitud, Pageable pageable);

    List<Reporte> findAllByUsuarioId(String usuarioId);
    

    List<Reporte> findByCategoriaAndFechaCreacionBetween(String categoria, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    String categoria(String categoria);

    List<Reporte> findByUsuarioId(String usuarioId);
}

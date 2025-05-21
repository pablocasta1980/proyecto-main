package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest // ✅ Optimiza las pruebas para MongoDB
public class ReporteRepoTest {

    @Autowired
    private ReporteRepo reporteRepo;

    @Test
    void testBuscarPorCategoria() {
        Page<Reporte> reportes = reporteRepo.findByCategoria("MASCOTAS", PageRequest.of(0, 5));
        assertNotNull(reportes);
        assertFalse(reportes.isEmpty());
    }

    @Test
    void testBuscarPorUsuarioOrdenadoFecha() {
        List<Reporte> reportes = reporteRepo.findByUsuarioIdOrderByFechaCreacionAsc("65123456789abcdef0123456");
        assertNotNull(reportes);
        assertFalse(reportes.isEmpty());
    }

    @Test
    void testBuscarPorNombre() {
        Page<Reporte> reportes = reporteRepo.buscarPorNombre("robo", PageRequest.of(0, 5));
        assertNotNull(reportes);
        assertFalse(reportes.isEmpty());
    }
}

package co.edu.uniquindio.proyecto.test.model.impl;

import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReporteTest {

    @Autowired
    private ReporteRepo reporteRepo;

    @Test
    public void registrarReporteTest() {
        // Crear un reporte con sus propiedades
        Reporte reporte = Reporte.builder()
                .nombre("Reporte de accidente")
                .descripcion("Descripción del reporte de accidente")
                .categoria("Accidentes")
                .usuarioId(new ObjectId("67fa7c179f09ed43b63abc63").toString()) // Asumiendo que usuarioId es de tipo String
                .fechaCreacion(LocalDateTime.now())
                .fechaRechazo(null) // Puede ser null si no ha sido rechazado
                .conteoImportante(5)
                .estado(EstadoReporte.PENDIENTE)
                .motivoCambioEstado(null) // Si es un estado inicial, no tiene motivo
                .build();

        // Guardar el reporte en la base de datos
        Reporte reporteCreado = reporteRepo.save(reporte);

        // Verificar que el reporte se ha guardado correctamente
        assertNotNull(reporteCreado, "El reporte no fue guardado correctamente");
        assertEquals("Reporte de accidente", reporteCreado.getNombre(), "El nombre del reporte no es correcto");
    }

    @Test
    public void actualizarReporteTest() {
        // Crear un reporte
        Reporte reporte = Reporte.builder()
                .nombre("Reporte de accidente")
                .descripcion("Descripción del reporte de accidente")
                .categoria("Accidentes")
                .usuarioId(new ObjectId("67fa7c179f09ed43b63abc63").toString())
                .fechaCreacion(LocalDateTime.now())
                .fechaRechazo(null)
                .conteoImportante(6)
                .estado(EstadoReporte.PENDIENTE)
                .motivoCambioEstado(null)
                .build();

        // Guardar el reporte en la base de datos
        Reporte reporteCreado = reporteRepo.save(reporte);

        // Actualizar un campo
        reporteCreado.setDescripcion("Descripción actualizada del reporte");
        Reporte reporteActualizado = reporteRepo.save(reporteCreado);

        // Verificar que el reporte se actualizó correctamente
        assertEquals("Descripción actualizada del reporte", reporteActualizado.getDescripcion());
    }

    @Test
    public void eliminarReporteTest() {
        // Obtener todos los reportes
        List<Reporte> reportes = reporteRepo.findAll();

        // Eliminar el primer reporte (suponiendo que existe)
        if (!reportes.isEmpty()) {
            Reporte reporteAEliminar = reportes.get(0);
            reporteRepo.delete(reporteAEliminar);
            Optional<Reporte> reporteBuscado = reporteRepo.findById(reporteAEliminar.getId());
            assertFalse(reporteBuscado.isPresent(), "El reporte no fue eliminado correctamente");
        }
    }

    @Test
    public void buscarReporteTest() {
        // Buscar un reporte por su ID (suponiendo que conoces el ID)
        Optional<Reporte> reporteOptional = reporteRepo.findById(new ObjectId("67fbc3b1a1c9b045eaa957ad").toString());

        // Verificar que el reporte existe
        assertTrue(reporteOptional.isPresent(), "El reporte no fue encontrado");

        // Verificar los valores del reporte
        Reporte reporte = reporteOptional.get();
        assertEquals("Reporte de accidente", reporte.getNombre(), "El nombre del reporte no es correcto");
    }

    @Test
    public void mostrarReportesTest() {
        // Obtener todos los reportes
        List<Reporte> reportes = reporteRepo.findAll();

        // Imprimir información de cada reporte
        for (Reporte reporte : reportes) {
            System.out.println("Id: " + reporte.getId() + ", Nombre: " + reporte.getNombre() + ", Estado: " + reporte.getEstado());
        }
    }
}
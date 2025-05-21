package co.edu.uniquindio.proyecto.test.model.impl;

import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.repositorios.ComentarioRepositorio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ComentarioTest {

    @Autowired
    private ComentarioRepositorio comentarioRepo;

    @Test
    public void registrarComentarioTest() {
        Comentario comentario = Comentario.builder()
                .comentarioTexto("Excelente reporte, gracias por compartir.")
                .fecha(LocalDateTime.now())
                .usuarioId(new ObjectId("67fa7c179f09ed43b63abc63").toString()) // Simula un ID de usuario
                .reporteId(new ObjectId("67fbc3b1a1c9b045eaa957ad").toString()) // Simula un ID de reporte
                .build();

        Comentario comentarioGuardado = comentarioRepo.save(comentario);
        assertNotNull(comentarioGuardado);
        System.out.println("Comentario guardado con ID: " + comentarioGuardado.getId());
    }

    @Test
    public void actualizarComentarioTest() {
        Comentario comentario = Comentario.builder()
                .comentarioTexto("Comentario inicial")
                .fecha(LocalDateTime.now())
                .usuarioId(new ObjectId("67fa7c179f09ed43b63abc63").toString()) // Simula un ID de usuario
                .reporteId(new ObjectId("67fbc3b1a1c9b045eaa957ad").toString()) // Simula un ID de reporte
                .build();

        Comentario guardado = comentarioRepo.save(comentario);

        // Actualizar el comentario
        guardado.setComentarioTexto("Comentario actualizado");
        Comentario actualizado = comentarioRepo.save(guardado);

        assertEquals("Comentario actualizado", actualizado.getComentarioTexto());
    }

    @Test
    public void eliminarComentarioTest() {
        List<Comentario> comentarios = comentarioRepo.findAll();

        // Eliminar el primer comentario (suponiendo que existe)
        if (!comentarios.isEmpty()) {
            Comentario comentarioAEliminar = comentarios.get(0);
            comentarioRepo.delete(comentarioAEliminar);
            Optional<Comentario> comentarioBuscado = comentarioRepo.findById(comentarioAEliminar.getId());
            assertFalse(comentarioBuscado.isPresent(), "El reporte no fue eliminado correctamente");
        }
    }

    @Test
    public void buscarComentarioPorIdTest() {
        // Asegúrate de tener un ID válido aquí si ejecutas esta prueba con datos reales
        String id = "67fbc5b0948aa442398db776";

        Optional<Comentario> comentarioOptional = comentarioRepo.findById(id);
        if (comentarioOptional.isPresent()) {
            Comentario comentario = comentarioOptional.get();
            System.out.println("Comentario encontrado: " + comentario.getComentarioTexto());
        } else {
            System.out.println("No se encontró el comentario con ID: " + id);
        }
    }

    @Test
    public void mostrarComentariosTest() {
        List<Comentario> comentarios = comentarioRepo.findAll();
        for (Comentario c : comentarios) {
            System.out.println("Id: " + c.getId() +
                    ", Texto: " + c.getComentarioTexto() +
                    ", Usuario: " + c.getUsuarioId() +
                    ", Reporte: " + c.getReporteId());
        }
    }
}

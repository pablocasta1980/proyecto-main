package co.edu.uniquindio.proyecto.test.model.impl;

import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoriaTest {

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Test
    public void registrarCategoriaTest() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Accidente armenia");
        categoria.setDescripcion("Reporte de accidente armenia");

        Categoria categoriaGuardada = categoriaRepo.save(categoria);
        assertNotNull(categoriaGuardada, "La categoría no fue guardada correctamente");
    }

    @Test
    public void actualizarCategoriaTest() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Accidente armenia");
        categoria.setDescripcion("Reporte de accidente en armenia");

        Categoria guardada = categoriaRepo.save(categoria);
        guardada.setDescripcion("Reporte de accidentes");

        assertEquals("Reporte de accidentes", guardada.getDescripcion());
    }

    @Test
    public void eliminarCategoriaTest() {
        List<Categoria> categorias = categoriaRepo.findAll();

        Categoria primercategoria = categorias.getFirst();
        categoriaRepo.delete(primercategoria);

    }

    @Test
    public void buscarCategoriaTest() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Accidente armenia");
        categoria.setDescripcion("Reporte de accidente en armenia");

        Categoria guardada = categoriaRepo.save(categoria);

        Optional<Categoria> encontrada = categoriaRepo.findById(guardada.getId());
        assertTrue(encontrada.isPresent(), "No se encontró la categoría");
        System.err.println("Categoría encontrada: " + encontrada.get().getNombre());
    }

    @Test
    public void mostrarCategoriasTest() {
        List<Categoria> categorias = categoriaRepo.findAll();
        for (Categoria categoria : categorias) {
            System.err.println("ID: " + categoria.getId() +
                    ", Nombre: " + categoria.getNombre() +
                    ", Descripción: " + categoria.getDescripcion());
        }
        assertFalse(categorias.isEmpty(), "No se encontraron categorías en la base de datos");
    }
}

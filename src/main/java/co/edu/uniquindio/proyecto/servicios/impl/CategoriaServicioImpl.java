package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.ActualizarCategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CrearCategoriaDTO;
import co.edu.uniquindio.proyecto.excepciones.RecursoNoEncontradoException;
import co.edu.uniquindio.proyecto.mapper.CategoriaMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServicioImpl implements CategoriaServicio {

    private final CategoriaRepo categoriaRepo;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaDTO crearCategoria(CrearCategoriaDTO dto) throws Exception {
        if (categoriaRepo.existsByNombre(dto.nombre().toUpperCase())) {
            throw new Exception("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.nombre().toUpperCase())
                .descripcion(dto.descripcion())
                .build();

        Categoria guardada = categoriaRepo.save(categoria);
        return categoriaMapper.toDTO(guardada);
    }


    @Override
    public void actualizarCategoria(String id, ActualizarCategoriaDTO dto) throws Exception {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));

        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        categoria.setNombre(dto.nombre().toUpperCase());
        categoria.setDescripcion(dto.descripcion());

        categoriaRepo.save(categoria);
    }

    @Override
    public void eliminarCategoria(String id) throws Exception {
        if (!categoriaRepo.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada");
        }
        categoriaRepo.deleteById(id);
    }

    @Override
    public CategoriaDTO obtenerCategoria(String id) throws Exception {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la categoría no puede estar vacío");
        }

        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id));

        return categoriaMapper.toDTO(categoria);
    }

    @Override
    public List<CategoriaDTO> listarCategorias() throws Exception {
        List<Categoria> categorias = categoriaRepo.findAll();

        if (categorias.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay categorías registradas.");
        }

        return categorias.stream()
                .map(categoria -> new CategoriaDTO(
                        categoria.getId(),
                        categoria.getNombre(),
                        categoria.getDescripcion()
                ))
                .collect(Collectors.toList());
    }

}


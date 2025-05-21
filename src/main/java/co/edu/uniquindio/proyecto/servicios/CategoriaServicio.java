package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.ActualizarCategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.CrearCategoriaDTO;

import java.util.List;

public interface CategoriaServicio {

    CategoriaDTO crearCategoria(CrearCategoriaDTO dto) throws Exception;

    void actualizarCategoria(String id, ActualizarCategoriaDTO categoria) throws Exception;

    void eliminarCategoria(String id) throws Exception;

    CategoriaDTO obtenerCategoria(String id) throws Exception;

    List<CategoriaDTO> listarCategorias() throws Exception;
}

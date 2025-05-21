package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;

import java.util.List;

public interface ComentarioServicio {
    Comentario crearComentario(String idReporte, CrearComentarioDTO dto) throws Exception;

    List<Comentario> obtenerComentariosPorIdReporte(String idReporte) throws Exception;

}




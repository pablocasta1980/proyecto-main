package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComentarioRepositorio extends MongoRepository<Comentario, String> {
    List<Comentario> findByReporteId(String idReporte);
}


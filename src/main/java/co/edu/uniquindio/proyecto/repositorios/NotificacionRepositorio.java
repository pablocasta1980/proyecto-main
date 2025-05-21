package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Notificacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificacionRepositorio extends MongoRepository<Notificacion, String> {
    List<Notificacion> findAllByIdUsuarioOrderByFechaDesc(String idUsuario);
}

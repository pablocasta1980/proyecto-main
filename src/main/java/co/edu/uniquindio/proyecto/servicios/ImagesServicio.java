package co.edu.uniquindio.proyecto.servicios;


import org.springframework.web.multipart.MultipartFile;
import java.util.Map;


public interface ImagesServicio {


    Map subirImagen(MultipartFile images) throws Exception;

    Map eliminarImagen(String idImages) throws Exception;
}
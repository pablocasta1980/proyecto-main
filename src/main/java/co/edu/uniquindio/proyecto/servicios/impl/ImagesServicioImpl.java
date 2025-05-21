package co.edu.uniquindio.proyecto.servicios.impl;


import co.edu.uniquindio.proyecto.servicios.ImagesServicio;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class ImagesServicioImpl implements ImagesServicio {


    private final Cloudinary cloudinary;

    public ImagesServicioImpl(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dtkoazswy");
        config.put("api_key", "962215592364787");
        config.put("api_secret", "BTJtnLJe8B9qP4G1BLlJtD5cMOo");


        cloudinary = new Cloudinary(config);
    }


    @Override
    public Map subirImagen(MultipartFile imagen) throws Exception {
        File file = convertir(imagen);
        return cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "reportes"));
    }


    @Override
    public Map eliminarImagen(String idImagen) throws Exception {
        return cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
    }


    private File convertir(MultipartFile imagen) throws IOException {
        File file = File.createTempFile(imagen.getOriginalFilename(), null);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imagen.getBytes());
        fos.close();
        return file;
    }


}

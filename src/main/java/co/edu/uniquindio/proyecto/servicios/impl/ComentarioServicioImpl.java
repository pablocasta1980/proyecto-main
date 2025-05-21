package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.NotificacionDTO;
import co.edu.uniquindio.proyecto.excepciones.RecursoNoEncontradoException;
import co.edu.uniquindio.proyecto.mapper.ComentarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.repositorios.ComentarioRepositorio;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.servicios.ComentarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServicioImpl implements ComentarioServicio {

    private final ComentarioRepositorio comentarioRepo;
    private final ReporteRepo reporteRepo;
    private final ComentarioMapper comentarioMapper;
    private final WebSocketNotificationService webSocketNotificationService;


    @Override
    public Comentario crearComentario(String idReporte, CrearComentarioDTO dto) throws Exception {

        // Obtener usuario autenticado
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        // Verificar existencia del reporte
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reporte no encontrado con id: " + idReporte));

        // Mapeo del DTO
        Comentario comentario = comentarioMapper.crearDtoToComentario(dto);

        // Asignar campos que ignoramos en el mapper
        comentarioMapper.completarDatos(comentario, idUsuario, idReporte);
        comentario.setFecha(LocalDateTime.now());

        webSocketNotificationService.notificarClientes( new NotificacionDTO(
                "",
                "",
                "/topic/reports"
        ) );


        return comentarioRepo.save(comentario);
    }


    @Override
    public List<Comentario> obtenerComentariosPorIdReporte(String idReporte) throws Exception {

        // Verificar si existe el reporte
        if (!reporteRepo.existsById(idReporte)) {
            throw new RecursoNoEncontradoException("Reporte no encontrado con id: " + idReporte);
        }

        // Obtener los comentarios por ID de reporte
        return comentarioRepo.findByReporteId(idReporte);
    }

}

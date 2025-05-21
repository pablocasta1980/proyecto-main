package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reportes")
public class ReporteControlador {

    private final ReporteServicio reporteServicio;
    private final ReporteMapper reporteMapper;
    private final JWTUtils jwtUtils;


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearReporte(@Valid @RequestBody CrearReporteDTO crearReporteDTO) throws Exception {
        // Guardar el reporte
        Reporte reporteGuardado = reporteServicio.crearReporte(crearReporteDTO);

        // Responder con mensaje de éxito
        return ResponseEntity.status(201)
                .body(new MensajeDTO<>(false, "Reporte creado exitosamente: " + reporteGuardado.getId(), null));
    }



    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reporte> reportesPage = reporteServicio.listarReportes(pageable);
        return ResponseEntity.ok(reportesPage.getContent());  // Devuelve solo el contenido de la página
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtenerReportePorId(@PathVariable String id) throws Exception{
        Reporte reporte = reporteServicio.obtenerReportePorId(id);
        ReporteDTO reporteDTO = reporteMapper.toDTO(reporte);
        return ResponseEntity.ok(reporteDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> editarReporte(@PathVariable String id,
                                                            @Valid @RequestBody EditarReporteDTO editarReporteDTO) throws Exception {
            reporteServicio.editarReporte(id, editarReporteDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte actualizado exitosamente", null));
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarReporte(@PathVariable String id)   throws Exception{
        reporteServicio.eliminarReporte(id);
        return ResponseEntity.ok(new MensajeDTO<String>(false, "Reporte eliminado exitosamente",null));
    }



    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/importante")
    public ResponseEntity<MensajeDTO<String>> marcarImportante(@PathVariable String id) throws  Exception{

        // Llamar al servicio
        boolean resultado = reporteServicio.marcarImportante(id);

        if (resultado) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte marcado como importante", null));
        } else {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(true, "Ya marcaste este reporte o eres el creador", null));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<MensajeDTO<String>> cambiarEstado(
            @PathVariable String id,
            @RequestBody EstadoReporteDTO dto) throws Exception {
        boolean actualizado = reporteServicio.cambiarEstado(id, dto);

        return ResponseEntity.ok(new MensajeDTO<>(false, "Estado actualizado correctamente.", null));
    }


    // mostrar el informe en PDF en el navegador
    @GetMapping(value = "/informe", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarInformePDF(
            @RequestParam String categoria,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) throws  Exception{

        byte[] pdf = reporteServicio.generarInformePDF(categoria, fechaInicio, fechaFin) ;

        //Descargar pdf
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    // visualizar los reportes en la web
    @GetMapping("/filtrados")
    public ResponseEntity<List<ReporteDTO>> listarReportesPorCategoriaYFechas(
            @RequestParam String categoria,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<ReporteDTO> reportes = reporteServicio.listarReportesPorCategoriaYFechas(categoria, fechaInicio, fechaFin);
        return ResponseEntity.ok(reportes);
    }

    @PostMapping("/cercanos")
    public ResponseEntity<List<ReporteDTO>> obtenerCercanos(@RequestBody UbicacionDTO ubicacionDTO) {
        Pageable pageable = PageRequest.of(0, 10); // Paginación básica

        Page<Reporte> pagina = reporteServicio.obtenerReportesCercanos(
                ubicacionDTO.longitud(),
                ubicacionDTO.latitud(),
                pageable
        );

        List<ReporteDTO> reportes = pagina.getContent().stream()
                .map(reporteMapper::toDTO)
                .toList();

        return ResponseEntity.ok(reportes);
    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/buscar")
    public ResponseEntity<MensajeDTO<List<ReporteDTO>>> buscarReportes(@Valid BuscarReporteDTO filtros) throws Exception {
        List<ReporteDTO> resultados = reporteServicio.buscarReportes(filtros);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lista de reportes filtrados obtenida correctamente", resultados));
    }


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/calificacion")
    public ResponseEntity<MensajeDTO<String>> calificarReporte(
            @PathVariable String id,
            @Valid @RequestBody CalificacionDTO dto) throws Exception {

        reporteServicio.calificarReporte(id, dto.estrellas());

        return ResponseEntity.ok(
                new MensajeDTO<>(false, "Calificación registrada correctamente", null)
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/mis-reportes")
    public ResponseEntity<List<ReporteDTO>> obtenerMisReportes() throws Exception {
        List<ReporteDTO> reportes = reporteServicio.obtenerReportesPorUsuario();
        return ResponseEntity.ok(reportes);
    }



}
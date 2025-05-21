package co.edu.uniquindio.proyecto.excepciones;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.ValidacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MensajeDTO<String>> noResourceFoundExceptionHandler(NoResourceFoundException ex) {
        return ResponseEntity.status(404).body(new MensajeDTO<>(true, "El recurso no fue encontrado", null));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<MensajeDTO<String>> recursoNoEncontradoHandler(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(404).body(new MensajeDTO<>(true, "Recurso no encontrado", ex.getMessage()));
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<MensajeDTO<String>> accesoNoAutorizadoHandler(AccesoNoAutorizadoException ex) {
        return ResponseEntity.status(403).body(new MensajeDTO<>(true, "Acceso denegado", ex.getMessage()));
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<MensajeDTO<String>> validacionExceptionHandler(ValidacionException ex) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, "Error de validación", ex.getMessage()));
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<MensajeDTO<String>> operacionNoPermitidaHandler(OperacionNoPermitidaException ex) {
        return ResponseEntity.status(409).body(new MensajeDTO<>(true, "Operación no permitida", ex.getMessage()));
    }

    @ExceptionHandler(AccionDuplicadaException.class)
    public ResponseEntity<MensajeDTO<String>> accionDuplicadaHandler(AccionDuplicadaException ex) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, "Acción duplicada", ex.getMessage()));
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ResponseEntity<MensajeDTO<String>> estadoInvalidoHandler(EstadoInvalidoException ex) {
        return ResponseEntity.status(409).body(new MensajeDTO<>(true, "Estado no válido", ex.getMessage()));
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<MensajeDTO<String>> tokenExpiradoHandler(TokenExpiradoException ex) {
        return ResponseEntity.status(401).body(new MensajeDTO<>(true, "Token expirado", ex.getMessage()));
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<MensajeDTO<String>> tokenInvalidoHandler(TokenInvalidoException ex) {
        return ResponseEntity.status(400).body(new MensajeDTO<>(true, "Token inválido", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeDTO<List<ValidacionDTO>>> validationExceptionHandler(MethodArgumentNotValidException ex) {
        List<ValidacionDTO> errores = new ArrayList<>();
        BindingResult results = ex.getBindingResult();

        for (FieldError e : results.getFieldErrors()) {
            errores.add(new ValidacionDTO(e.getField(), e.getDefaultMessage()));
        }

        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, "Errores de validación", errores));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO<String>> generalExceptionHandler(Exception e) {
        return ResponseEntity.internalServerError().body(new MensajeDTO<>(true, "Error interno", e.getMessage()));
    }


    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<MensajeDTO<String>> manejarParametroInvalidoException(ParametroInvalidoException e) {
        return ResponseEntity
                .badRequest()
                .body(new MensajeDTO<>(false, "Parámetro inválido en la solicitud",e.getMessage()));
    }

}

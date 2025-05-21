package co.edu.uniquindio.proyecto.excepciones;

public class AccionDuplicadaException extends RuntimeException {
    public AccionDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
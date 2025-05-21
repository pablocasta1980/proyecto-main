// EstadoReporteDTO.java

package co.edu.uniquindio.proyecto.dto;

public class EstadoReporteDTO {

    private String nuevoEstado;  // El nuevo estado que se quiere asignar
    private String motivo;       // El motivo del cambio de estado

    // Constructor
    public EstadoReporteDTO(String nuevoEstado, String motivo) {
        this.nuevoEstado = nuevoEstado;
        this.motivo = motivo;
    }

    // Getters y Setters
    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

package co.edu.uniquindio.proyecto.dto;

public record CambioPasswordConTokenDTO(
        String token,
        String email,
        String nuevaPassword) {
}

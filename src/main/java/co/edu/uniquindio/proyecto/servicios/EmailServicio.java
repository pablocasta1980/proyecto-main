package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface EmailServicio {
    void enviarCorreo(EmailDTO emailDTO) throws MessagingException;
}

package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.CambioPasswordConTokenDTO;
import co.edu.uniquindio.proyecto.dto.LoginDTO;
import co.edu.uniquindio.proyecto.dto.RecuperarPasswordDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;

public interface AutenticacionServicio {

   TokenDTO login(LoginDTO loginDTO) throws Exception;
   boolean recuperarPassword(RecuperarPasswordDTO recuperarPasswordDTO) throws Exception;
   String cambiarPasswordConToken(CambioPasswordConTokenDTO dto) throws Exception;
}

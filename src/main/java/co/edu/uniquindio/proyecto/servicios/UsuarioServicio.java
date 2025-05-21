package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.RecursoNoEncontradoException;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;

import java.util.List;

public interface UsuarioServicio {

    void crear(CrearUsuarioDTO cuenta) throws Exception;

    void eliminar(String id) throws Exception;

    void editarCuenta(String id, EditarUsuarioDTO cuenta) throws Exception;


    UsuarioDTO obtener(String id) throws Exception;

    List<UsuarioDTO> listarTodos(String nombre, String ciudad, int pagina);

    boolean activarCuenta(String email, String codigo);

    boolean cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception;

    //TokenDTO generarToken(CrearUsuarioDTO usuarioDTO) throws Exception;


    GenerarTokenDTO generarToken(String email);

    void reenviarToken(String email) throws Exception;

    void actualizarSuscripcionNotificaciones(String idUsuario, SuscripcionNotificacionesDTO suscripcion) throws Exception;

    Usuario obtenerPorId(String id) throws RecursoNoEncontradoException;
}
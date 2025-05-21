package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.dto.CrearUsuarioDTO;
import co.edu.uniquindio.proyecto.dto.EditarUsuarioDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsuarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void crearCorrectoTest() throws Exception {

        //Se crea un objeto para realizar la creación de la cuenta
        CrearUsuarioDTO cuentaDTO = new CrearUsuarioDTO(
                "felipe",
                "28289292",
                Ciudad.PEREIRA,
                "2Carrera 10 # 20-30",
                "2luis@email.com",
                "2hshjsjhs7s88998"
        );

        //Se realiza la petición POST al servidor usando el MockMvc y se valida que el estado de la respuesta sea 201
        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    public void crearIncorrectoTest() throws Exception {

        //Se crea un objeto para realizar la creación de la cuenta
        CrearUsuarioDTO cuentaDTO = new CrearUsuarioDTO(
                "felipe",
                "3117812222",
                Ciudad.ARMENIA,
                "Carrera 10 # 20-30",
                "carlosandil.com",
                "password123"
        );

        //Se realiza la petición POST al servidor usando el MockMvc y se valida que el estado de la respuesta sea 201
        mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void actualizarTest() throws Exception {

        //Se crea un token de autenticación para realizar la prueba
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJyb2wiOiJST0xFX0NMSUVOVEUiLCJlbWFpbCI6InJvYmltYXJrZXQxNUBnbWFpbC5jb20iLCJub21icmUiOiJNaWNoYWVsIFN0ZXZlbiBPbGl2YSIsInN1YiI6IjY3ZmE3YzE3OWYwOWVkNDNiNjNhYmM2MyIsImlhdCI6MTc0NDU0NzY3MCwiZXhwIjoxNzQ0NTc3NjcwfQ.w4cyX2TJIL3-e3eD9Y4yJfsgtPFLuKgDLdFD-72sxewQPoPxukp1YWEEU81wTSM6";
        String id = "67fba6d76c21781ec94c26b7";

//        String token = jwtUtils.generateToken(usuario.getId().toString(), crearClaims(usuario), 500);

        //Se crea un objeto para realizar la actualización de los datos de la cuenta
        EditarUsuarioDTO cuentaDTO = new EditarUsuarioDTO(
                "ARMENIA",
                "Calle 20 2 2",
                "3117812222"
        );

        //Se realiza la petición PUT al servidor usando el MockMvc y se valida que el estado de la respuesta sea OK
        mockMvc.perform(put("/api/usuarios/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void eliminarTest() throws Exception {

        //Se crea un token de autenticación para realizar la prueba
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJyb2wiOiJST0xFX0NMSUVOVEUiLCJlbWFpbCI6InJvYmltYXJrZXQxNUBnbWFpbC5jb20iLCJub21icmUiOiJNaWNoYWVsIFN0ZXZlbiBPbGl2YSIsInN1YiI6IjY3ZmE3YzE3OWYwOWVkNDNiNjNhYmM2MyIsImlhdCI6MTc0NDU0NzY3MCwiZXhwIjoxNzQ0NTc3NjcwfQ.w4cyX2TJIL3-e3eD9Y4yJfsgtPFLuKgDLdFD-72sxewQPoPxukp1YWEEU81wTSM6";
        String id = "67fba6d76c21781ec94c26b7";

        //Se realiza la petición DELETE al servidor usando el MockMvc y se valida que el estado de la respuesta sea OK
        mockMvc.perform(delete("/api/usuarios/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void obtenerTest() throws Exception {

        //Se crea un token de autenticación para realizar la prueba
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJyb2wiOiJST0xFX0NMSUVOVEUiLCJlbWFpbCI6InJvYmltYXJrZXQxNUBnbWFpbC5jb20iLCJub21icmUiOiJNaWNoYWVsIFN0ZXZlbiBPbGl2YSIsInN1YiI6IjY3ZmE3YzE3OWYwOWVkNDNiNjNhYmM2MyIsImlhdCI6MTc0NDU0NzY3MCwiZXhwIjoxNzQ0NTc3NjcwfQ.w4cyX2TJIL3-e3eD9Y4yJfsgtPFLuKgDLdFD-72sxewQPoPxukp1YWEEU81wTSM6";
        String id = "67fba6d76c21781ec94c26b7";

        //Se realiza la petición GET al servidor usando el MockMvc y se valida que el estado de la respuesta sea OK
        mockMvc.perform(get("/api/usuarios/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void obtenerTodosTest() throws Exception {

        //Se realiza la petición GET al servidor usando el MockMvc y se valida que el estado de la respuesta sea OK
        mockMvc.perform(get("/api/usuarios?pagina=0&nombre=felipe")
                        .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}

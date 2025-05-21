package co.edu.uniquindio.proyecto.test.model.impl;

import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioTest {

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private View error;

    @Test
    public void registrarTest() {
        //Creamos un cliente con sus propiedades
        Usuario usuario = Usuario.builder()
                .nombre("Sergio Ramirez")
                .ciudad(Ciudad.ARMENIA)
                .direccion("calle 23 # 12 7")
                .email("sergio@email.com")
                .telefono("3212325439")
                .password("123456")
                .fechaRegistro(LocalDateTime.now())
                .rol(Rol.CLIENTE)
                .estado(EstadoUsuario.INACTIVO)
                .build();

        //Guardamos el cliente en la base de datos
        Usuario clienteCreado = usuarioRepo.save(usuario);

        //Verificamos que se haya guardado validando que no sea null
        assertNotNull(clienteCreado, "El cliente no fue guardado correctamente");
    }

    @Test
    public void actualizarTest() {
        Usuario usuario = Usuario.builder()
                .nombre("Sergio Ramirez")
                .ciudad(Ciudad.PEREIRA)
                .direccion("calle 12 # 22 27")
                .email("sergio@email.com")
                .telefono("3212325439")
                .password("123456")
                .fechaRegistro(LocalDateTime.now())
                .rol(Rol.CLIENTE)
                .estado(EstadoUsuario.INACTIVO)
                .build();
        Usuario clienteCreado = usuarioRepo.save(usuario);
        clienteCreado.setTelefono("2212123");
        usuarioRepo.save(usuario);
    }

    @Test
    public void eliminarTest() {

        List<Usuario> usuarios = usuarioRepo.findAll();
        Usuario primerUsuario = usuarios.getFirst();
        usuarioRepo.delete(primerUsuario);

        usuarioRepo.deleteById(new ObjectId("67f9900df0f3d205c210ae1d'"));  //otra opcion de eliminar dado el id
    }

    @Test
    public void buscarUsuarioTest() {

        // Forma 1 usando if
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(new ObjectId("67f9900df0f3d205c210ae1d"));
        if (usuarioOptional.isPresent()) {

            System.err.println("Usuario: "+usuarioOptional.get().getNombre());
            System.err.println("Correo: "+usuarioOptional.get().getEmail());
        } else {
            System.err.println("No se encontro el usuario");
        }

        // Forma 2 que usa una validacion interna con el orElseThrow
        Usuario usuarioOptional2 = usuarioRepo.
                findById(new ObjectId("67f9900df0f3d205c210ae1d"))
                .orElseThrow( () -> new RuntimeException("No se encontro el usuario"));
        System.out.println(usuarioOptional2);

    }

    @Test
    public void mostrarUsuariosTest() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        for (Usuario usuario : usuarios) {
            System.err.println("Id: "+usuario.getId()+", Usuario: "+usuario.getNombre()+", correo: "+usuario.getEmail());
        }
    }
}
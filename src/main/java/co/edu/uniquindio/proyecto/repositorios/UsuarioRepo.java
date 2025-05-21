package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends MongoRepository<Usuario, ObjectId> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTokenActivacion(String token);

    Optional<Usuario> findById(String id);
}

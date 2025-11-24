package com.milsaboresbackend.milsaboresapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.milsaboresbackend.milsaboresapp.model.RolUsuario;
import com.milsaboresbackend.milsaboresapp.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Buscar usuario por email (login)
    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    //Verificar si existe usuario por email
    boolean existsByEmailUsuario(String emailUsuario);

    //Buscar usuarios por rol
    List<Usuario> findByRolUsuario(RolUsuario rolUsuario);

    //Buscar clientes específicamente
    default List<Usuario> findClientes() {
        return findByRolUsuario(RolUsuario.CLIENTE);
    }

    //Buscar administradores específicamente
    default List<Usuario> findAdministradores() {
        return findByRolUsuario(RolUsuario.ADMIN);
    }    

    //Consulta login
    @Query("SELECT u FROM Usuario u WHERE u.emailUsuario = :emailUsuario AND u.passwordUsuario = :passwordUsuario")
    Optional<Usuario> findByEmailPassword(@Param("emailUsuario") String emailUsuario, @Param("passwordUsuario") String passwordUsuario);

}

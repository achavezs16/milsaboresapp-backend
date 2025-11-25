package com.milsaboresbackend.milsaboresapp.service;

import com.milsaboresbackend.milsaboresapp.dtc.UsuarioCreateDTO;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioDTO;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioCreateDTOToUsuarioConverter;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioToUsuarioDTOConverter;
import com.milsaboresbackend.milsaboresapp.model.RolUsuario;
import com.milsaboresbackend.milsaboresapp.model.Usuario;
import com.milsaboresbackend.milsaboresapp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioToUsuarioDTOConverter toDtoConverter;
    private final UsuarioCreateDTOToUsuarioConverter toEntityConverter;
    private final PasswordEncoder passwordEncoder;

    //Obtener todos los usuarios
    public List<UsuarioDTO> findAllUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    //Obtener usuario por ID
    public Optional<UsuarioDTO> findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .map(toDtoConverter::convert);
    }

    //CRUD
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        // Validar que el email no exista
        if (usuarioRepository.existsByEmailUsuario(usuarioCreateDTO.getEmailUsuario())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Si no se especifica rol, por defecto es CLIENTE
        if (usuarioCreateDTO.getRolUsuario() == null) {
            usuarioCreateDTO.setRolUsuario(RolUsuario.CLIENTE);
        }

        Usuario usuario = toEntityConverter.convert(usuarioCreateDTO);

        String passwordEncriptada = passwordEncoder.encode(usuarioCreateDTO.getPasswordUsuario());
        usuario.setPasswordUsuario(passwordEncriptada);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return toDtoConverter.convert(savedUsuario);
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //Validar que el email no esté usado por otro usuario
        if (!existingUsuario.getEmailUsuario().equals(usuarioCreateDTO.getEmailUsuario()) &&
            usuarioRepository.existsByEmailUsuario(usuarioCreateDTO.getEmailUsuario())) {
            throw new RuntimeException("El email ya está registrado por otro usuario");
        }

        //Actualizar campos
        existingUsuario.setNombreUsuario(usuarioCreateDTO.getNombreUsuario());
        existingUsuario.setApellidoUsuario(usuarioCreateDTO.getApellidoUsuario());
        existingUsuario.setEmailUsuario(usuarioCreateDTO.getEmailUsuario());

        if (usuarioCreateDTO.getPasswordUsuario() != null && !usuarioCreateDTO.getPasswordUsuario().isEmpty()) {
            String passwordEncriptada = passwordEncoder.encode(usuarioCreateDTO.getPasswordUsuario());
            existingUsuario.setPasswordUsuario(passwordEncriptada);
        }
        existingUsuario.setRolUsuario(usuarioCreateDTO.getRolUsuario());

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return toDtoConverter.convert(updatedUsuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    //MÉTODOS DE NEGOCIO (BÚSQUEDAS ESPECÍFICAS)
    //Buscar usuario por email (para login)
    public Optional<UsuarioDTO> findByEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email)
                .map(toDtoConverter::convert);
    }

    //Obtener usuarios por rol
    public List<UsuarioDTO> findByRol(RolUsuario rol) {
        return usuarioRepository.findByRolUsuario(rol)
                .stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    //Obtener todos los clientes
    public List<UsuarioDTO> findAllClientes() {
        return usuarioRepository.findClientes()
                .stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    //Obtener todos los administradores
    public List<UsuarioDTO> findAllAdministradores() {
        return usuarioRepository.findAdministradores()
                .stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    //Validar credenciales (para login)
    public Optional<UsuarioDTO> validateLogin(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getPasswordUsuario())) {
                return Optional.of(toDtoConverter.convert(usuario));
            }
        }
        return Optional.empty();
    }

    //Verificar si existe usuario por ID
    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }
}

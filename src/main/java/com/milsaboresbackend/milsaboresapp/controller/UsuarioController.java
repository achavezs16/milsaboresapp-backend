package com.milsaboresbackend.milsaboresapp.controller;

import com.milsaboresbackend.milsaboresapp.dtc.LoginResponseDTO;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioCreateDTO;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioDTO;
import com.milsaboresbackend.milsaboresapp.model.RolUsuario;
import com.milsaboresbackend.milsaboresapp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    //Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.findAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    //Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.findUsuarioById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    //Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuarioCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, 
                                         @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioCreateDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().body("Usuario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Buscar usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioEmail(@PathVariable String email) {
        Optional<UsuarioDTO> usuario = usuarioService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    //Obtener usuarios por rol
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosRol(@PathVariable RolUsuario rolUsuario) {
        List<UsuarioDTO> usuarios = usuarioService.findByRol(rolUsuario);
        return ResponseEntity.ok(usuarios);
    }

    //Obtener todos los clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioDTO>> listarClientes() {
        List<UsuarioDTO> clientes = usuarioService.findAllClientes();
        return ResponseEntity.ok(clientes);
    }

    //Obtener todos los administradores
    @GetMapping("/administradores")
    public ResponseEntity<List<UsuarioDTO>> listarAdministradores() {
        List<UsuarioDTO> administradores = usuarioService.findAllAdministradores();
        return ResponseEntity.ok(administradores);
    }

    //Endpoint específico para login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestParam String emailUsuario, 
                                                @RequestParam String passwordUsuario) {
        Optional<UsuarioDTO> usuario = usuarioService.validateLogin(emailUsuario, passwordUsuario);
        
        if (usuario.isPresent()) {
            return ResponseEntity.ok(LoginResponseDTO.success(usuario.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(LoginResponseDTO.error("Credenciales inválidas"));
        }
    }

    // ✅ Verificar si existe usuario por ID
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> usuarioExists(@PathVariable Long id) {
        boolean exists = usuarioService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}

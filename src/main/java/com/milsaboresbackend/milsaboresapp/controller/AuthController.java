package com.milsaboresbackend.milsaboresapp.controller;

import com.milsaboresbackend.milsaboresapp.dtc.LoginRequestDTO;
import com.milsaboresbackend.milsaboresapp.dtc.UsuarioDTO;
import com.milsaboresbackend.milsaboresapp.security.JwtTokenProvider;
import com.milsaboresbackend.milsaboresapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioService usuarioService;

    // ✅ Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // 1. Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmailUsuario(),
                    loginRequest.getPasswordUsuario()
                )
            );

            // 2. Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Generar token JWT
            String token = jwtTokenProvider.generarToken(authentication);

            // 4. Obtener datos del usuario
            Optional<UsuarioDTO> usuarioDTO = usuarioService.findByEmail(loginRequest.getEmailUsuario());

            if (usuarioDTO.isPresent()) {
                // 5. Construir respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("tipo", "Bearer");
                response.put("usuario", usuarioDTO.get());
                response.put("mensaje", "Login exitoso");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener datos del usuario");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas: " + e.getMessage());
        }
    }

    // ✅ Endpoint para verificar token (útil para el frontend)
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            
            Optional<UsuarioDTO> usuarioDTO = usuarioService.findByEmail(authentication.getName());
            
            if (usuarioDTO.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("autenticado", true);
                response.put("usuario", usuarioDTO.get());
                return ResponseEntity.ok(response);
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("autenticado", false));
    }

    // ✅ Endpoint para logout (simbólico - el frontend elimina el token)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("mensaje", "Logout exitoso"));
    }
}

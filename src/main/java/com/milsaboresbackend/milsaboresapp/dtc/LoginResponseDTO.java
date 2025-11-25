package com.milsaboresbackend.milsaboresapp.dtc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private boolean success;
    private String message;
    private UsuarioDTO usuario;
    
    //Mensaje éxito
    public static LoginResponseDTO success(UsuarioDTO usuario) {
        return new LoginResponseDTO(true, "Login exitoso", usuario);
    }
    
    //Mensaje error
    public static LoginResponseDTO error(String message) {
        return new LoginResponseDTO(false, message, null);
    }
}

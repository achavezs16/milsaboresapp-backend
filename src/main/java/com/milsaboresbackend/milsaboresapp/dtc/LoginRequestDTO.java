package com.milsaboresbackend.milsaboresapp.dtc;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    
    @NotBlank(message = "El email es obligatorio")
    private String emailUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String passwordUsuario;
}

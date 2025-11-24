package com.milsaboresbackend.milsaboresapp.dtc;

import com.milsaboresbackend.milsaboresapp.model.RolUsuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioCreateDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 200)
    private String nombreUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 200)
    private String apellidoUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 300)
    private String emailUsuario;    

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 300)
    private String passwordUsuario;    

    private RolUsuario rolUsuario;
    
}

package com.milsaboresbackend.milsaboresapp.dtc;

import com.milsaboresbackend.milsaboresapp.model.RolUsuario;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String emailUsuario;
    private RolUsuario rolUsuario;
    
}

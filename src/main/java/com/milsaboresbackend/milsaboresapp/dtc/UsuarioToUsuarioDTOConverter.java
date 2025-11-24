package com.milsaboresbackend.milsaboresapp.dtc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.milsaboresbackend.milsaboresapp.model.Usuario;

@Component
public class UsuarioToUsuarioDTOConverter implements Converter<Usuario, UsuarioDTO> {

    @Override
    public UsuarioDTO convert(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setApellidoUsuario(usuario.getApellidoUsuario());
        dto.setEmailUsuario(usuario.getEmailUsuario());
        dto.setRolUsuario(usuario.getRolUsuario());
        return dto;
    }
    
}

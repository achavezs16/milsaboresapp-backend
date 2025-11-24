package com.milsaboresbackend.milsaboresapp.dtc;

import org.springframework.core.convert.converter.Converter;

import com.milsaboresbackend.milsaboresapp.model.Usuario;

public class UsuarioCreateDTOToUsuarioConverter implements Converter<UsuarioCreateDTO, Usuario> {

    @Override
    public Usuario convert(UsuarioCreateDTO dto) {
        return Usuario.builder()
                .nombreUsuario(dto.getNombreUsuario())
                .apellidoUsuario(dto.getApellidoUsuario())
                .emailUsuario(dto.getEmailUsuario())
                .passwordUsuario(dto.getPasswordUsuario())
                .rolUsuario(dto.getRolUsuario())
                .build();
    }
    
}

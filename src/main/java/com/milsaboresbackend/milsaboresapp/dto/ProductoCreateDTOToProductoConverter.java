package com.milsaboresbackend.milsaboresapp.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.milsaboresbackend.milsaboresapp.model.Producto;

@Component
public class ProductoCreateDTOToProductoConverter implements Converter<ProductoCreateDTO, Producto> {

    @Override
    @Nullable
    public Producto convert(ProductoCreateDTO productoCreateDTO) {

        Producto producto = new Producto();
        producto.setCategProd(productoCreateDTO.getCategProd());
        producto.setNombreProd(productoCreateDTO.getNombreProd());
        producto.setDescProd(productoCreateDTO.getDescProd());
        producto.setPrecioProd(productoCreateDTO.getPrecioProd());
        producto.setImagenProd(productoCreateDTO.getImagenProd());

        return producto;
    }
    
}

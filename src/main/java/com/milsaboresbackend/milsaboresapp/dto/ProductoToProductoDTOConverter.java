package com.milsaboresbackend.milsaboresapp.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.milsaboresbackend.milsaboresapp.model.Producto;

@Component
public class ProductoToProductoDTOConverter implements Converter<Producto, ProductoDTO> {

    @Override
    @Nullable
    public ProductoDTO convert(Producto producto) {

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdProd(producto.getIdProd());
        productoDTO.setCategProd(producto.getCategProd());
        productoDTO.setNombreProd(producto.getNombreProd());
        productoDTO.setDescProd(producto.getDescProd());
        productoDTO.setPrecioProd(producto.getPrecioProd());
        productoDTO.setImagenProd(producto.getImagenProd());
        productoDTO.setProductoDestacado(producto.getProductoDestacado());

        return productoDTO;
    }
    

    
}
package com.milsaboresbackend.milsaboresapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milsaboresbackend.milsaboresapp.dto.ProductoDTO;
import com.milsaboresbackend.milsaboresapp.dto.ProductoToProductoDTOConverter;
import com.milsaboresbackend.milsaboresapp.model.Producto;
import com.milsaboresbackend.milsaboresapp.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoToProductoDTOConverter productoToProductoDTOConverter;

    //CRUD (CREATE-READ-UPDATE-DELETE)
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto obtenerProductoPorId(Long idProd) {
        return productoRepository.findById(idProd).orElse(null);
    }

    public ProductoDTO buscarProductoPorId(Long idProd) { 
        return productoRepository.findById(idProd)
                .map(productoToProductoDTOConverter::convert)
                .orElse(null);
    }

    public List<ProductoDTO> listarTodosProductos() {
        List<Producto> listaProductos = productoRepository.findAll();
        List<ProductoDTO> productoDTOs = new ArrayList<>();

        for (Producto producto : listaProductos) { 
            ProductoDTO dto = productoToProductoDTOConverter.convert(producto);
            productoDTOs.add(dto);
        }
        return productoDTOs;
    }

    public Producto actualizarProducto(Long idProd, Producto producto) {
        if (productoRepository.existsById(idProd)) {
            producto.setIdProd(idProd);
            return productoRepository.save(producto);
        }
        return null;
    }

    public void eliminarProductoPorId(Long idProd) {
        productoRepository.deleteById(idProd);
    }

    //MÉTODOS DE NEGOCIO (FILTRADO POR CATEGORÍA, NOMBRE, PRECIO)
    public List<ProductoDTO> buscarPorCategoria(String categProd) {
        List<Producto> listaProductos = productoRepository.findByCategProd(categProd);
        List<ProductoDTO> productoDTOs = new ArrayList<>();

        for (Producto producto : listaProductos) {
            productoDTOs.add(productoToProductoDTOConverter.convert(producto));
        }
        return productoDTOs;
    }

    public List<ProductoDTO> buscarPorNombre(String nombreProd) {
        List<Producto> listaProductos = productoRepository.findByNombreProdContainingIgnoreCase(nombreProd);
        List<ProductoDTO> productoDTOs = new ArrayList<>();

        for (Producto producto : listaProductos) {
            productoDTOs.add(productoToProductoDTOConverter.convert(producto));
        }
        return productoDTOs;
    }

    public List<ProductoDTO> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        List<Producto> listaProductos = productoRepository.findByPrecioProdBetween(precioMin, precioMax);
        List<ProductoDTO> productoDTOs = new ArrayList<>();

        for (Producto producto : listaProductos) {
            productoDTOs.add(productoToProductoDTOConverter.convert(producto));
        }
        return productoDTOs;
    }

    public List<String> obtenerCategorias() {
        List<Producto> productos = productoRepository.findAll();
        List<String> categorias = new ArrayList<>();

        for (Producto producto : productos) {
            if (!categorias.contains(producto.getCategProd())) {
                categorias.add(producto.getCategProd());
            }
        }
        return categorias;
    }

    public List<Producto> obtenerProductosDestacados() {
        return productoRepository.findByProductoDestacadoTrue();
    }
    
}

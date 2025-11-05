package com.milsaboresbackend.milsaboresapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milsaboresbackend.milsaboresapp.dto.ProductoCreateDTO;
import com.milsaboresbackend.milsaboresapp.dto.ProductoCreateDTOToProductoConverter;
import com.milsaboresbackend.milsaboresapp.dto.ProductoDTO;
import com.milsaboresbackend.milsaboresapp.dto.ProductoToProductoDTOConverter;
import com.milsaboresbackend.milsaboresapp.model.Producto;
import com.milsaboresbackend.milsaboresapp.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoCreateDTOToProductoConverter createDTOToProductoConverter;
    private final ProductoToProductoDTOConverter productoToDTOConverter;

    @GetMapping("/status")
    public String getStatus() {
        return "Mil Sabores API está conectada! 🎂";
    }

    @GetMapping
    public ResponseEntity<?> listarTodosProductos() {
        try {
            List<ProductoDTO> productosDTO = productoService.listarTodosProductos();
            
            if (productosDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No hay productos disponibles");
            }
            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener productos: " + e.getMessage());
        }
    }

    @GetMapping("/{idProd}")
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Long idProd) {
        try {
            ProductoDTO productoDTO = productoService.buscarProductoPorId(idProd);

            if (productoDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Producto con ID " + idProd + " no encontrado.");
            }
            return ResponseEntity.ok(productoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el producto: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoCreateDTO productoCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errores = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                    .orElse("Datos inválidos");
            return ResponseEntity.badRequest().body("Errores en los datos: " + errores);
        }

        try {
            Producto producto = createDTOToProductoConverter.convert(productoCreateDTO);
            Producto productoCreado = productoService.crearProducto(producto);
            ProductoDTO productoDTO = productoToDTOConverter.convert(productoCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(productoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el producto: " + e.getMessage());
        }
    }
    

    
    
    

    
}

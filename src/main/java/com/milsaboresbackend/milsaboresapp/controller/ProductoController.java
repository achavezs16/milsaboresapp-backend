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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @PutMapping("/{idProd}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long idProd, @Valid @RequestBody ProductoCreateDTO productoCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errores = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                    .orElse("Datos inválidos");
            return ResponseEntity.badRequest().body("Errores en los datos: " + errores);
        }
        
        try {
            Producto producto = createDTOToProductoConverter.convert(productoCreateDTO);
            Producto productoActualizado = productoService.actualizarProducto(idProd, producto);

            if (productoActualizado != null) {
                ProductoDTO productoDTO = productoToDTOConverter.convert(productoActualizado);
                return ResponseEntity.ok(productoDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con ID " + idProd + " no encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el producto: " + e.getMessage());
        }
    } 

    @DeleteMapping("/{idProd}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long idProd) { 
        try {
            Producto producto = productoService.obtenerProductoPorId(idProd);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró producto con ID: " + idProd);
            }

            ProductoDTO productoDTO = productoToDTOConverter.convert(producto);
            productoService.eliminarProductoPorId(idProd);

            return ResponseEntity.ok().body(
                Map.of(
                    "mensaje", "Producto eliminado correctamente", "productoEliminado", productoDTO
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar producto: " + e.getMessage());
        }
    }    

    //ENDPOINT DE NEGOCIO (FILTRADO POR CATEGORIA, NOMBRE, PRECIO)
    //Filtrado por categoría
    @GetMapping("/categoria/{categProd}")
    public ResponseEntity<?> buscarPorCategoria(@PathVariable String categProd) {
        try {
            List<ProductoDTO> productosDTO = productoService.buscarPorCategoria(categProd);
            if (productosDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay productos en la categoría: " + categProd);
            } 
            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar productos por categoría: " + e.getMessage());
        }
    }
    
    //Buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<ProductoDTO> productosDTO = productoService.buscarPorNombre(nombre);
            if (productosDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No se encontraron productos con: " + nombre);
            }
            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos: " + e.getMessage());
        }
    }

    //Buscar por rango de precio
    @GetMapping("/precio")
    public ResponseEntity<?> buscarPorRangoPrecio(@RequestParam Double precioMin, @RequestParam Double precioMax) {
        try {
            List<ProductoDTO> productosDTO = productoService.buscarPorRangoPrecio(precioMin, precioMax);
            if (productosDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay productos en el rango de precio $" + precioMin + " - $" + precioMax);
            }
            return ResponseEntity.ok(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar productos por precio: " + e.getMessage());
        }
    }

    //Obtener todas las categorias
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        try {
            List<String> categorias = productoService.obtenerCategorias();
            if (categorias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay categorías disponibles");
            }
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener las categorías: " + e.getMessage());
        }
    }
    
    
    
    

    
}

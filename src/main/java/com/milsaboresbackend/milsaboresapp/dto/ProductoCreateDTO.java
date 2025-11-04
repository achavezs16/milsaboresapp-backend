package com.milsaboresbackend.milsaboresapp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreateDTO {

    @NotBlank(message = "La categoría del producto es obligatoria")
    @Size(max = 100, message = "La categoría del producto no puede exceder los 100 caracteres")
    private String categProd;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre del producto no puede exceder los 200 caracteres")
    private String nombreProd;

    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(max = 500, message = "La descripción del producto no puede exceder los 500 caracteres")
    private String descProd;

    @NotNull(message = "El precio del producto es obligatorio")
    @Positive(message = "El precio del producto debe ser mayor a 0")
    private Double precioProd;

    private String imagenProd;
    
}

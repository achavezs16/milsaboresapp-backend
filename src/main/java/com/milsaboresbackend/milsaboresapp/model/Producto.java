package com.milsaboresbackend.milsaboresapp.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProd;

    @NotBlank(message = "La categoría del producto es obligatoria")
    @Size(max = 100, message = "La categoría del producto no puede exceder los 100 caracteres")
    @Column(name = "categ_prod", nullable = false, length = 100)
    private String categProd;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre del producto no puede exceder los 200 caracteres")
    @Column(name = "nombre_prod", nullable = false, length = 100)
    private String nombreProd;

    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(max = 500, message = "La descripción del producto no puede exceder los 500 caracteres")
    @Column(name = "desc_prod", nullable = false, length = 500)
    private String descProd;

    @NotNull(message = "El precio del producto es obligatorio")
    @Positive(message = "El precio del producto debe ser mayor a 0")
    @Column(name = "precio_prod", nullable = false)
    private Double precioProd;

    @Column(name = "imagen_prod", length = 300)
    private String imagenProd;
    
    
}

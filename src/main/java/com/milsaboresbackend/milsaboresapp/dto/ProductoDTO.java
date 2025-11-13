package com.milsaboresbackend.milsaboresapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long idProd;
    private String categProd;
    private String nombreProd;
    private String descProd;
    private Double precioProd;
    private String imagenProd;
    private Boolean productoDestacado;
}

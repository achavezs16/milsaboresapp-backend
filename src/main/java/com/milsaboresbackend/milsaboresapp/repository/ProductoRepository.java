package com.milsaboresbackend.milsaboresapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milsaboresbackend.milsaboresapp.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    //Busca productos por categoria
    List<Producto> findByCategProd(String categProd);

    //Busca productos por nombre (contiene)
    List<Producto> findByNombreProdContainingIgnoreCase(String nombreProd);

    //Buscar producto por nombre exacto
    Optional<Producto> findByNombreProdIgnoreCase(String nombreProd);

    //Busca productos por rango de precio
    List<Producto> findByPrecioProdBetween(Double precioMin, Double precioMax);

    //Busca productos por categoria y ordena por precio
    List<Producto> findByCategProdOrderByPrecioProdAsc(String categProd);
    List<Producto> findByCategProdOrderByPrecioProdDesc(String categProd);

    //Consulta para verificar si existe producto con el mismo nombre
    boolean existsByNombreProd(String nombreProd);

    //Contar productos por categoria
    Long countByCategProd(String categProd);
    
}

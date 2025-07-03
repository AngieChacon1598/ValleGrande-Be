package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar productos por estado (activo/inactivo)
    List<Product> findByStatus(Boolean status);

    // Buscar productos por categoría
    List<Product> findByCategory_Id(Long categoryId);

    // Buscar productos activos por categoría
    List<Product> findByCategory_IdAndStatus(Long categoryId, Boolean status);
}

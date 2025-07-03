package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Método para obtener solo categorías activas
    List<Category> findByStatus(String status);
}
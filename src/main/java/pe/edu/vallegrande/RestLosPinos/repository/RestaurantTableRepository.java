package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {
    
    // Método para buscar mesas por estado
    List<RestaurantTable> findByState(String state);
    
    // Método para verificar si existe una mesa con el mismo número
    boolean existsByTableNumber(Integer tableNumber);
}
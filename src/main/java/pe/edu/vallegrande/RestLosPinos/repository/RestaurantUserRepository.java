package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;

import java.util.List;
import java.util.Optional;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Integer> {

    // Método para buscar usuarios por estado
    List<RestaurantUser> findByState(String state);
    
    // Método para verificar si existe un usuario con el mismo username
    boolean existsByUserName(String userName);
    
    // Método para buscar un usuario por su username (texto plano)
    Optional<RestaurantUser> findByUserName(String userName);
    
    // Método para verificar si existe un usuario con el mismo email
    boolean existsByEmail(String email);
}

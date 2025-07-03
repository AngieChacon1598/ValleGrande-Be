package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    // Métodos adicionales si son necesarios
} 
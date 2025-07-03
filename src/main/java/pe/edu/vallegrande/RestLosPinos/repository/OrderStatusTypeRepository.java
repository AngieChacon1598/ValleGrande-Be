package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.OrderStatusType;

public interface OrderStatusTypeRepository extends JpaRepository<OrderStatusType, Integer> {
    // Métodos adicionales si necesitas
}

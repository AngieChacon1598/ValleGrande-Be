package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.PaymentType;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer> {
    // Métodos adicionales si necesitas
}

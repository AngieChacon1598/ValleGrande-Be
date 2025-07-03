package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.Reservation;
import pe.edu.vallegrande.RestLosPinos.model.OrderStatusType;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
    // Método para buscar reservas por estado
    List<Reservation> findByOrderStatusType(OrderStatusType orderStatusType);
    
    // Método para buscar reservas por fecha
    List<Reservation> findByReservationDateBetween(Date startDate, Date endDate);
}
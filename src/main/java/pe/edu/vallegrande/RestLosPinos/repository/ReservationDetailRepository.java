package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.ReservationDetail;
import pe.edu.vallegrande.RestLosPinos.model.Reservation;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantTable;

import java.util.List;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Integer> {
    
    // Método para buscar detalles por reserva
    List<ReservationDetail> findByReservation(Reservation reservation);
    
    // Método para buscar detalles por mesa
    List<ReservationDetail> findByRestaurantTable(RestaurantTable restaurantTable);
}
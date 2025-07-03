package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.Reservation;
import pe.edu.vallegrande.RestLosPinos.model.OrderStatusType;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.model.dto.ReservationDTO;
import pe.edu.vallegrande.RestLosPinos.repository.ReservationRepository;
import pe.edu.vallegrande.RestLosPinos.repository.OrderStatusTypeRepository;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantUserRepository;

import java.util.Date;
import java.util.List;

import pe.edu.vallegrande.RestLosPinos.model.dto.ReservationDTO;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OrderStatusTypeRepository orderStatusTypeRepository;
    private final RestaurantUserRepository restaurantUserRepository;

    public ReservationService(ReservationRepository reservationRepository,
                             OrderStatusTypeRepository orderStatusTypeRepository,
                             RestaurantUserRepository restaurantUserRepository) {
        this.reservationRepository = reservationRepository;
        this.orderStatusTypeRepository = orderStatusTypeRepository;
        this.restaurantUserRepository = restaurantUserRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));
    }

    public List<Reservation> getReservationsByStatus(Integer statusId) {
        OrderStatusType statusType = orderStatusTypeRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de estado con ID " + statusId + " no encontrado"));
        return reservationRepository.findByOrderStatusType(statusType);
    }

    public List<Reservation> getReservationsByDateRange(Date startDate, Date endDate) {
        return reservationRepository.findByReservationDateBetween(startDate, endDate);
    }

    public Reservation saveReservation(Reservation reservation) {
        // Validar campos obligatorios
        if (reservation.getReservationName() == null || reservation.getReservationName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la reserva es obligatorio");
        }
        if (reservation.getReservationDate() == null) {
            throw new IllegalArgumentException("La fecha de reserva es obligatoria");
        }
        
        // Validar relaciones
        if (reservation.getOrderStatusType() != null && reservation.getOrderStatusType().getIdTypeState() != null) {
            OrderStatusType statusType = orderStatusTypeRepository.findById(reservation.getOrderStatusType().getIdTypeState())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de estado no encontrado"));
            reservation.setOrderStatusType(statusType);
        }
        
        if (reservation.getRestaurantUser() != null && reservation.getRestaurantUser().getUserId() != null) {
            RestaurantUser user = restaurantUserRepository.findById(reservation.getRestaurantUser().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            reservation.setRestaurantUser(user);
        }
        
        // Generar ID si es nuevo
        if (reservation.getReservationId() == null) {
            Integer nextId = getNextReservationId();
            reservation.setReservationId(nextId);
        }
        
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Integer id, Reservation reservationDetails) {
        Reservation reservation = getReservationById(id);
        
        // Actualizar campos
        if (reservationDetails.getReservationName() != null && !reservationDetails.getReservationName().trim().isEmpty()) {
            reservation.setReservationName(reservationDetails.getReservationName());
        }
        
        if (reservationDetails.getReservationDate() != null) {
            reservation.setReservationDate(reservationDetails.getReservationDate());
        }
        
        if (reservationDetails.getOrderStatusType() != null && reservationDetails.getOrderStatusType().getIdTypeState() != null) {
            OrderStatusType statusType = orderStatusTypeRepository.findById(reservationDetails.getOrderStatusType().getIdTypeState())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de estado no encontrado"));
            reservation.setOrderStatusType(statusType);
        }
        
        if (reservationDetails.getRestaurantUser() != null && reservationDetails.getRestaurantUser().getUserId() != null) {
            RestaurantUser user = restaurantUserRepository.findById(reservationDetails.getRestaurantUser().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            reservation.setRestaurantUser(user);
        }
        
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Integer id) {
        // Verificar que exista la reserva
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));
        
        // Buscar el estado "Cancelado" (ID 4 según los scripts SQL)
        OrderStatusType canceledStatus = orderStatusTypeRepository.findById(4)
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Cancelado' no encontrado"));
        
        // Actualizar el estado en lugar de eliminar
        reservation.setOrderStatusType(canceledStatus);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void restoreReservation(Integer id) {
        // Verificar que exista la reserva
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));
        
        // Buscar el estado "Pendiente" (ID 1 según los scripts SQL)
        OrderStatusType pendingStatus = orderStatusTypeRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Pendiente' no encontrado"));
        
        // Restaurar el estado
        reservation.setOrderStatusType(pendingStatus);
        reservationRepository.save(reservation);
    }

    // Método para obtener reservas canceladas (eliminadas lógicamente)
    public List<ReservationDTO> getCanceledReservationsDTO() {
        // Buscar el estado "Cancelado" (ID 4)
        OrderStatusType canceledStatus = orderStatusTypeRepository.findById(4)
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Cancelado' no encontrado"));
        
        List<Reservation> reservations = reservationRepository.findByOrderStatusType(canceledStatus);
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener reservas activas (no canceladas)
    public List<ReservationDTO> getActiveReservationsDTO() {
        // Excluir el estado "Cancelado" (ID 4)
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r -> r.getOrderStatusType() == null || r.getOrderStatusType().getIdTypeState() != 4)
                .collect(Collectors.toList());
        
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private Integer getNextReservationId() {
        return reservationRepository.findAll().stream()
                .map(Reservation::getReservationId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
    
    // Método para convertir entidad a DTO
    public ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setReservationName(reservation.getReservationName());
        dto.setReservationDate(reservation.getReservationDate());
        
        // Manejo seguro de relaciones
        if (reservation.getOrderStatusType() != null) {
            dto.setOrderStatusTypeId(reservation.getOrderStatusType().getIdTypeState());
            dto.setOrderStatusTypeName(reservation.getOrderStatusType().getName());
        }
        
        if (reservation.getRestaurantUser() != null) {
            dto.setUserId(reservation.getRestaurantUser().getUserId());
            dto.setUserName(reservation.getRestaurantUser().getUserName());
        }
        
        return dto;
    }
    
    // Métodos que devuelven DTOs en lugar de entidades
    public List<ReservationDTO> getAllReservationsDTO() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ReservationDTO getReservationDTOById(Integer id) {
        Reservation reservation = getReservationById(id);
        return convertToDTO(reservation);
    }
    
    public List<ReservationDTO> getReservationsDTOByStatus(Integer statusId) {
        List<Reservation> reservations = getReservationsByStatus(statusId);
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReservationDTO> getReservationsDTOByDateRange(Date startDate, Date endDate) {
        List<Reservation> reservations = getReservationsByDateRange(startDate, endDate);
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getReservationsDTOByUserId(Integer userId) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getRestaurantUser() != null && r.getRestaurantUser().getUserId().equals(userId))
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.ReservationDetail;
import pe.edu.vallegrande.RestLosPinos.model.Reservation;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantTable;
import pe.edu.vallegrande.RestLosPinos.model.dto.ReservationDetailDTO;
import pe.edu.vallegrande.RestLosPinos.repository.ReservationDetailRepository;
import pe.edu.vallegrande.RestLosPinos.repository.ReservationRepository;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantTableRepository;

import java.util.List;

import pe.edu.vallegrande.RestLosPinos.model.dto.ReservationDetailDTO;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationDetailService {

    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationDetailService(ReservationDetailRepository reservationDetailRepository,
                                   ReservationRepository reservationRepository,
                                   RestaurantTableRepository restaurantTableRepository) {
        this.reservationDetailRepository = reservationDetailRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public List<ReservationDetail> getAllReservationDetails() {
        return reservationDetailRepository.findAll();
    }

    public ReservationDetail getReservationDetailById(Integer id) {
        return reservationDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de reserva con ID " + id + " no encontrado"));
    }

    public List<ReservationDetail> getReservationDetailsByReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + reservationId + " no encontrada"));
        return reservationDetailRepository.findByReservation(reservation);
    }

    public List<ReservationDetail> getReservationDetailsByTable(Integer tableId) {
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con ID " + tableId + " no encontrada"));
        return reservationDetailRepository.findByRestaurantTable(table);
    }

    public ReservationDetail saveReservationDetail(ReservationDetail reservationDetail) {
        // Validar campos obligatorios
        if (reservationDetail.getNumberPeople() == null || reservationDetail.getNumberPeople() <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor que cero");
        }
        
        // Validar que no exceda el límite de personas
        if (reservationDetail.getNumberPeople() > 8) {
            throw new IllegalArgumentException("El número de personas no puede ser mayor a 8");
        }
        
        // Validar relaciones
        if (reservationDetail.getReservation() == null || reservationDetail.getReservation().getReservationId() == null) {
            throw new IllegalArgumentException("La reserva es obligatoria");
        }
        
        Reservation reservation = reservationRepository.findById(reservationDetail.getReservation().getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        reservationDetail.setReservation(reservation);
        
        if (reservationDetail.getRestaurantTable() != null && reservationDetail.getRestaurantTable().getTableId() != null) {
            RestaurantTable table = restaurantTableRepository.findById(reservationDetail.getRestaurantTable().getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
            reservationDetail.setRestaurantTable(table);
            
            // Validar que la mesa tenga capacidad suficiente
            if (table.getAbility() < reservationDetail.getNumberPeople()) {
                throw new IllegalArgumentException("La mesa seleccionada no tiene capacidad suficiente para el número de personas");
            }
        }
        
        // Generar ID si es nuevo
        if (reservationDetail.getIdReservationDetail() == null) {
            Integer nextId = getNextReservationDetailId();
            reservationDetail.setIdReservationDetail(nextId);
        }
        
        return reservationDetailRepository.save(reservationDetail);
    }

    public ReservationDetail updateReservationDetail(Integer id, ReservationDetail detailsToUpdate) {
        ReservationDetail reservationDetail = getReservationDetailById(id);
        
        // Actualizar campos
        if (detailsToUpdate.getNumberPeople() != null) {
            if (detailsToUpdate.getNumberPeople() <= 0) {
                throw new IllegalArgumentException("El número de personas debe ser mayor que cero");
            }
            if (detailsToUpdate.getNumberPeople() > 8) {
                throw new IllegalArgumentException("El número de personas no puede ser mayor a 8");
            }
            reservationDetail.setNumberPeople(detailsToUpdate.getNumberPeople());
            
            // Validar capacidad de la mesa si ya está asignada
            if (reservationDetail.getRestaurantTable() != null && 
                reservationDetail.getRestaurantTable().getAbility() < detailsToUpdate.getNumberPeople()) {
                throw new IllegalArgumentException("La mesa asignada no tiene capacidad suficiente para el nuevo número de personas");
            }
        }
        
        if (detailsToUpdate.getReservationMethod() != null) {
            reservationDetail.setReservationMethod(detailsToUpdate.getReservationMethod());
        }
        
        if (detailsToUpdate.getRequest() != null) {
            reservationDetail.setRequest(detailsToUpdate.getRequest());
        }
        
        if (detailsToUpdate.getRestaurantTable() != null && detailsToUpdate.getRestaurantTable().getTableId() != null) {
            RestaurantTable table = restaurantTableRepository.findById(detailsToUpdate.getRestaurantTable().getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
            
            // Validar capacidad de la nueva mesa
            if (table.getAbility() < reservationDetail.getNumberPeople()) {
                throw new IllegalArgumentException("La mesa seleccionada no tiene capacidad suficiente para el número de personas");
            }
            
            reservationDetail.setRestaurantTable(table);
        }
        
        if (detailsToUpdate.getReservation() != null && detailsToUpdate.getReservation().getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(detailsToUpdate.getReservation().getReservationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
            reservationDetail.setReservation(reservation);
        }
        
        return reservationDetailRepository.save(reservationDetail);
    }

    public void deleteReservationDetail(Integer id) {
        // Verificar que exista el detalle de reserva
        ReservationDetail detail = reservationDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de reserva con ID " + id + " no encontrado"));
        
        // Cambiar estado a inactivo en lugar de eliminar
        detail.setState("I");
        reservationDetailRepository.save(detail);
    }

    public void restoreReservationDetail(Integer id) {
        // Verificar que exista el detalle de reserva
        ReservationDetail detail = reservationDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de reserva con ID " + id + " no encontrado"));
        
        // Cambiar estado a activo
        detail.setState("A");
        reservationDetailRepository.save(detail);
    }

    public List<ReservationDetailDTO> getActiveReservationDetailsDTO() {
        List<ReservationDetail> details = reservationDetailRepository.findAll().stream()
                .filter(d -> "A".equals(d.getState()))
                .collect(Collectors.toList());
        
        return details.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDetailDTO> getInactiveReservationDetailsDTO() {
        List<ReservationDetail> details = reservationDetailRepository.findAll().stream()
                .filter(d -> "I".equals(d.getState()))
                .collect(Collectors.toList());
        
        return details.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Integer getNextReservationDetailId() {
        return reservationDetailRepository.findAll().stream()
                .map(ReservationDetail::getIdReservationDetail)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
    
    // Método para convertir entidad a DTO
    public ReservationDetailDTO convertToDTO(ReservationDetail detail) {
        ReservationDetailDTO dto = new ReservationDetailDTO();
        dto.setIdReservationDetail(detail.getIdReservationDetail());
        dto.setNumberPeople(detail.getNumberPeople());
        dto.setReservationMethod(detail.getReservationMethod());
        dto.setRequest(detail.getRequest());
        
        // Manejo seguro de relaciones
        if (detail.getRestaurantTable() != null) {
            dto.setTableId(detail.getRestaurantTable().getTableId());
            dto.setTableNumber(detail.getRestaurantTable().getTableNumber());
        }
        
        if (detail.getReservation() != null) {
            dto.setReservationId(detail.getReservation().getReservationId());
            dto.setReservationName(detail.getReservation().getReservationName());
        }
        
        return dto;
    }
    
    // Métodos que devuelven DTOs en lugar de entidades
    public List<ReservationDetailDTO> getAllReservationDetailsDTO() {
        return reservationDetailRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ReservationDetailDTO getReservationDetailDTOById(Integer id) {
        ReservationDetail detail = getReservationDetailById(id);
        return convertToDTO(detail);
    }
    
    public List<ReservationDetailDTO> getReservationDetailsDTOByReservation(Integer reservationId) {
        List<ReservationDetail> details = getReservationDetailsByReservation(reservationId);
        return details.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReservationDetailDTO> getReservationDetailsDTOByTable(Integer tableId) {
        List<ReservationDetail> details = getReservationDetailsByTable(tableId);
        return details.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
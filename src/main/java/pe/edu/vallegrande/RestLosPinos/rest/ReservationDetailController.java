package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.ReservationDetail;
import pe.edu.vallegrande.RestLosPinos.model.dto.ReservationDetailDTO;
import pe.edu.vallegrande.RestLosPinos.service.ReservationDetailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation-details")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationDetailController {

    private final ReservationDetailService reservationDetailService;

    public ReservationDetailController(ReservationDetailService reservationDetailService) {
        this.reservationDetailService = reservationDetailService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDetailDTO>> getAllReservationDetails() {
        List<ReservationDetailDTO> details = reservationDetailService.getAllReservationDetailsDTO();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailDTO> getReservationDetailById(@PathVariable Integer id) {
        ReservationDetailDTO detail = reservationDetailService.getReservationDetailDTOById(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<ReservationDetailDTO>> getReservationDetailsByReservation(@PathVariable Integer reservationId) {
        List<ReservationDetailDTO> details = reservationDetailService.getReservationDetailsDTOByReservation(reservationId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<ReservationDetailDTO>> getReservationDetailsByTable(@PathVariable Integer tableId) {
        List<ReservationDetailDTO> details = reservationDetailService.getReservationDetailsDTOByTable(tableId);
        return ResponseEntity.ok(details);
    }

    @PostMapping
    public ResponseEntity<?> createReservationDetail(@RequestBody ReservationDetail reservationDetail) {
        try {
            ReservationDetail savedDetail = reservationDetailService.saveReservationDetail(reservationDetail);
            ReservationDetailDTO dto = reservationDetailService.convertToDTO(savedDetail);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el detalle de reserva: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservationDetail(@PathVariable Integer id, @RequestBody ReservationDetail detailsToUpdate) {
        try {
            ReservationDetail updatedDetail = reservationDetailService.updateReservationDetail(id, detailsToUpdate);
            ReservationDetailDTO dto = reservationDetailService.convertToDTO(updatedDetail);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el detalle de reserva: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservationDetail(@PathVariable Integer id) {
        try {
            reservationDetailService.deleteReservationDetail(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Detalle de reserva eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el detalle de reserva: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReservationDetailDTO>> getActiveReservationDetails() {
        List<ReservationDetailDTO> details = reservationDetailService.getActiveReservationDetailsDTO();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ReservationDetailDTO>> getInactiveReservationDetails() {
        List<ReservationDetailDTO> details = reservationDetailService.getInactiveReservationDetailsDTO();
        return ResponseEntity.ok(details);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreReservationDetail(@PathVariable Integer id) {
        try {
            reservationDetailService.restoreReservationDetail(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Detalle de reserva restaurado con éxito");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al restaurar el detalle de reserva: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
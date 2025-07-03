package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;

@Data
public class ReservationDetailDTO {
    private Integer idReservationDetail;
    private Integer numberPeople;
    private String reservationMethod;
    private String request;
    private Integer tableId;
    private Integer tableNumber;
    private Integer reservationId;
    private String reservationName;
}
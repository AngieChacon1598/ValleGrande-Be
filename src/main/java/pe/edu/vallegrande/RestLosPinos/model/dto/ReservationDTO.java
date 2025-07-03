package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ReservationDTO {
    private Integer reservationId;
    private String reservationName;
    private Date reservationDate;
    private Integer orderStatusTypeId;
    private String orderStatusTypeName;
    private Integer userId;
    private String userName;
}
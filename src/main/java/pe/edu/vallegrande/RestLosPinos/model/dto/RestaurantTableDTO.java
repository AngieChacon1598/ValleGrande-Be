package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;

@Data
public class RestaurantTableDTO {
    private Integer tableId;
    private Integer tableNumber;
    private Integer ability;
    private String state;
}
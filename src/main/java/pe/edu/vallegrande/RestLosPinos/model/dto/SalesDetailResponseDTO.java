package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesDetailResponseDTO {
    private Integer amount;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
}

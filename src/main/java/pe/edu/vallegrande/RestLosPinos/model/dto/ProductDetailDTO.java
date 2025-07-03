package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDetailDTO {
    private Long id;
    private Integer amount;
    private Long productId;
    private Long salesTicketId;
    private String productName;
    private BigDecimal productPrice;
} 
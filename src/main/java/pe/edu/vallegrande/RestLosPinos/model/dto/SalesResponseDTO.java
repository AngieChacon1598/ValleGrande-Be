package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesResponseDTO {
    private Long ticketId;
    private LocalDateTime saleDate;
    private BigDecimal totalPayment;
    private String delivery;
    private String deliveryAddress;
    private String note;
    private RestaurantUserDTO user;  
    private OrderStatusTypeDTO orderStatusType;
    private PaymentTypeDTO paymentType;
    private List<SalesDetailResponseDTO> productDetails;
}

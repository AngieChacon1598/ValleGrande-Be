package pe.edu.vallegrande.RestLosPinos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesRequestDTO {
    private LocalDateTime saleDate;
    private BigDecimal totalPayment;
    private String delivery;
    private String deliveryAddress;
    private String note;
    
    @JsonProperty("user")
    private UserIdDTO user;
    
    @JsonProperty("orderStatusType")
    private OrderStatusTypeDTO orderStatusType;
    
    @JsonProperty("paymentType")
    private PaymentTypeDTO paymentType;

    private List<SalesDetailDTO> productDetails;

    // Clases internas para la deserialización
    @Data
    public static class UserIdDTO {
        private Integer userId;
    }

    @Data
    public static class OrderStatusTypeDTO {
        private Integer idTypeState;
    }

    @Data
    public static class PaymentTypeDTO {
        private Integer idPaymentType;
    }

    // Métodos de conveniencia para obtener los IDs
    public Integer getUserId() {
        return user != null ? user.getUserId() : null;
    }

    public Integer getIdTypeState() {
        return orderStatusType != null ? orderStatusType.getIdTypeState() : null;
    }

    public Integer getIdPaymentType() {
        return paymentType != null ? paymentType.getIdPaymentType() : null;
    }
}
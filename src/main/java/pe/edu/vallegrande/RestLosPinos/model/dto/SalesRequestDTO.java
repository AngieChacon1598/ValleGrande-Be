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
    
    // Campos para formato simple
    private Integer userId;
    private Integer id_type_state;
    private Integer id_payment_type;
    
    // Campos para formato complejo (objetos anidados)
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
        // Priorizar formato simple, luego formato complejo
        if (userId != null) {
            return userId;
        }
        return user != null ? user.getUserId() : null;
    }

    public Integer getIdTypeState() {
        // Priorizar formato simple, luego formato complejo
        if (id_type_state != null) {
            return id_type_state;
        }
        return orderStatusType != null ? orderStatusType.getIdTypeState() : null;
    }

    public Integer getIdPaymentType() {
        // Priorizar formato simple, luego formato complejo
        if (id_payment_type != null) {
            return id_payment_type;
        }
        return paymentType != null ? paymentType.getIdPaymentType() : null;
    }
}
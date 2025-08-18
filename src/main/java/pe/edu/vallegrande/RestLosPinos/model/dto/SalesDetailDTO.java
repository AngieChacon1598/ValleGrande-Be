package pe.edu.vallegrande.RestLosPinos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SalesDetailDTO {
    private Integer amount;

    // Campo para formato simple
    private Long productId;

    // Campo para formato complejo (objeto anidado)
    @JsonProperty("product")
    private ProductIdDTO product;

    @Data
    public static class ProductIdDTO {
        private Long id;
    }
    
    public Long getProductId() {
        // Priorizar formato simple, luego formato complejo
        if (productId != null) {
            return productId;
        }
        return product != null ? product.getId() : null;
    }
}

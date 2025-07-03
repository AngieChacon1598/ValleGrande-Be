package pe.edu.vallegrande.RestLosPinos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SalesDetailDTO {
    private Integer amount;

    @JsonProperty("product")
    private ProductIdDTO product;

    @Data
    public static class ProductIdDTO {
        private Long id;
    }
    
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
}

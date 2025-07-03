package pe.edu.vallegrande.RestLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.model.dto.ProductDetailDTO;
import pe.edu.vallegrande.RestLosPinos.service.ProductDetailService;

import java.util.List;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProductDetail(@RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO created = productDetailService.createProductDetail(productDetailDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailDTO>> getAllProductDetails() {
        List<ProductDetailDTO> details = productDetailService.getAllProductDetails();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetailById(@PathVariable Long id) {
        ProductDetailDTO detail = productDetailService.getProductDetailById(id);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO updated = productDetailService.updateProductDetail(id, productDetailDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long id) {
        productDetailService.deleteProductDetail(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/physical")
    public ResponseEntity<Void> deleteProductDetailPhysically(@PathVariable Long id) {
        productDetailService.deleteProductDetailPhysically(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales-ticket/{salesTicketId}")
    public ResponseEntity<List<ProductDetailDTO>> getProductDetailsBySalesTicket(@PathVariable Long salesTicketId) {
        List<ProductDetailDTO> details = productDetailService.getProductDetailsBySalesTicket(salesTicketId);
        return ResponseEntity.ok(details);
    }
} 
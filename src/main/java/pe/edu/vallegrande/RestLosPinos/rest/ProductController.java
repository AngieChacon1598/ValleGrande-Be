package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/inactive")
    public List<Product> getInactiveProducts() {
        return productService.getInactiveProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/physically/{id}")
    public ResponseEntity<?> deleteProductPhysically(@PathVariable Long id) {
        productService.deleteProductPhysically(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Product> restoreProduct(@PathVariable Long id) {
        Product restoredProduct = productService.restoreProduct(id);
        return ResponseEntity.ok(restoredProduct);
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<byte[]> generateProductReportPdf(@RequestParam(name = "estado", defaultValue = "1") int estado) {
        try {
            byte[] pdf = productService.generateJasperProductReportPdf(estado);
            String nombre = (estado == 1) ? "productos_activos.pdf" : "productos_inactivos.pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombre)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace(); // <--- Esto ya imprime el error en los logs
            return ResponseEntity.internalServerError().build();
        }
    }

    // Nuevos endpoints de filtrado para app móvil
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/category/{categoryId}/active")
    public List<Product> getActiveProductsByCategory(@PathVariable Long categoryId) {
        return productService.getActiveProductsByCategory(categoryId);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }
}

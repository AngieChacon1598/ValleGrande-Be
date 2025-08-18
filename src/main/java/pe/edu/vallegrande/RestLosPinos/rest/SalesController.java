package pe.edu.vallegrande.RestLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.RestLosPinos.model.dto.SalesRequestDTO;
import pe.edu.vallegrande.RestLosPinos.model.dto.SalesResponseDTO;
import pe.edu.vallegrande.RestLosPinos.service.SalesService;
import pe.edu.vallegrande.RestLosPinos.util.SecurityUtil;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<SalesResponseDTO> createSale(@RequestBody SalesRequestDTO salesRequestDTO) {
        SalesResponseDTO createdSale = salesService.createSale(salesRequestDTO);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesResponseDTO>> getAllSales() {
        List<SalesResponseDTO> sales = salesService.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> getSaleById(@PathVariable Long id) {
        SalesResponseDTO sale = salesService.getSaleById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SalesResponseDTO>> getSalesByUserId(@PathVariable Integer userId) {
        // Get current user's ID and role
        Integer currentUserId = SecurityUtil.getCurrentUserId();
        String currentUserRole = SecurityUtil.getCurrentUserRole();
        
        // Check if user is trying to access their own sales or is an admin
        if (!"Administrador".equals(currentUserRole) && !currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<SalesResponseDTO> sales = salesService.getSalesByUserId(userId);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> updateSale(@PathVariable Long id, @RequestBody SalesRequestDTO salesRequestDTO) {
        SalesResponseDTO updatedSale = salesService.updateSale(id, salesRequestDTO);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreSale(@PathVariable Long id) {
        salesService.restoreSale(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<SalesResponseDTO>> getInactiveSales() {
        List<SalesResponseDTO> sales = salesService.getInactiveSales();
        return ResponseEntity.ok(sales);
    }

    @DeleteMapping("/{id}/physical")
    public ResponseEntity<Void> deleteSalePhysically(@PathVariable Long id) {
        salesService.deleteSalePhysically(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateJasperPdfReport() {
        try {
            byte[] pdf = salesService.generateJasperPdfReport();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ventas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pdf/{ticketId}")
    public ResponseEntity<byte[]> generateJasperPdfReportPorTicket(@PathVariable Long ticketId) {
        try {
            byte[] pdf = salesService.generateJasperPdfReportPorTicket(ticketId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ticket_" + ticketId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/debug/products")
    public ResponseEntity<String> debugProducts() {
        try {
            // Obtener todos los productos para debug
            List<Product> products = productRepository.findAll();
            StringBuilder debug = new StringBuilder();
            debug.append("=== PRODUCTOS DISPONIBLES ===\n");
            for (Product product : products) {
                debug.append("ID: ").append(product.getId())
                     .append(", Nombre: ").append(product.getName())
                     .append(", Precio: ").append(product.getPrice())
                     .append(", Estado: ").append(product.getStatus())
                     .append("\n");
            }
            return ResponseEntity.ok(debug.toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}

package pe.edu.vallegrande.RestLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.RestLosPinos.model.PaymentType;
import pe.edu.vallegrande.RestLosPinos.service.PaymentTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/payment-types")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @GetMapping
    public ResponseEntity<List<PaymentType>> getAllPaymentTypes() {
        List<PaymentType> paymentTypes = paymentTypeService.getAllPaymentTypes();
        return ResponseEntity.ok(paymentTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentType> getPaymentTypeById(@PathVariable Integer id) {
        PaymentType paymentType = paymentTypeService.getPaymentTypeById(id);
        return ResponseEntity.ok(paymentType);
    }
} 
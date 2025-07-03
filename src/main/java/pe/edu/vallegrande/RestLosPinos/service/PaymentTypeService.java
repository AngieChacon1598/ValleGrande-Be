package pe.edu.vallegrande.RestLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.PaymentType;
import pe.edu.vallegrande.RestLosPinos.repository.PaymentTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    public List<PaymentType> getAllPaymentTypes() {
        return paymentTypeRepository.findAll();
    }

    public PaymentType getPaymentTypeById(Integer id) {
        return paymentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de pago con ID " + id + " no encontrado"));
    }
} 
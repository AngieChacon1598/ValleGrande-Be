package pe.edu.vallegrande.RestLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.OrderStatusType;
import pe.edu.vallegrande.RestLosPinos.repository.OrderStatusTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusTypeService {

    private final OrderStatusTypeRepository orderStatusTypeRepository;

    public List<OrderStatusType> getAllOrderStatusTypes() {
        return orderStatusTypeRepository.findAll();
    }

    public OrderStatusType getOrderStatusTypeById(Integer id) {
        return orderStatusTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de estado de orden con ID " + id + " no encontrado"));
    }
} 
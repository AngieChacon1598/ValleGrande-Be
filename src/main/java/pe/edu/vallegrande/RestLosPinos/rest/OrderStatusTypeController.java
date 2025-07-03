package pe.edu.vallegrande.RestLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.RestLosPinos.model.OrderStatusType;
import pe.edu.vallegrande.RestLosPinos.service.OrderStatusTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/order-status-types")
@RequiredArgsConstructor
public class OrderStatusTypeController {

    private final OrderStatusTypeService orderStatusTypeService;

    @GetMapping
    public ResponseEntity<List<OrderStatusType>> getAllOrderStatusTypes() {
        List<OrderStatusType> orderStatusTypes = orderStatusTypeService.getAllOrderStatusTypes();
        return ResponseEntity.ok(orderStatusTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusType> getOrderStatusTypeById(@PathVariable Integer id) {
        OrderStatusType orderStatusType = orderStatusTypeService.getOrderStatusTypeById(id);
        return ResponseEntity.ok(orderStatusType);
    }
} 
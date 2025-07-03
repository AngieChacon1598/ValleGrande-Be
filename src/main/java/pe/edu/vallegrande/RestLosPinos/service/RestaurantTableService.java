package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantTable;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantTableRepository;

import java.util.List;

@Service
@Transactional
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public List<RestaurantTable> getTablesByState(String state) {
        return restaurantTableRepository.findByState(state);
    }

    public RestaurantTable getTableById(Integer id) {
        return restaurantTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con ID " + id + " no encontrada"));
    }

    public RestaurantTable saveTable(RestaurantTable table) {
        // Validar campos obligatorios
        if (table.getTableNumber() == null) {
            throw new IllegalArgumentException("El número de mesa es obligatorio");
        }
        if (table.getAbility() == null || table.getAbility() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        
        // Validar que el número de mesa no exista
        if (table.getTableId() == null && restaurantTableRepository.existsByTableNumber(table.getTableNumber())) {
            throw new IllegalArgumentException("Ya existe una mesa con el número " + table.getTableNumber());
        }
        
        // Establecer estado por defecto si no se proporciona
        if (table.getState() == null) {
            table.setState("A");
        }
        
        // Generar ID si es nuevo
        if (table.getTableId() == null) {
            Integer nextId = getNextTableId();
            table.setTableId(nextId);
        }
        
        return restaurantTableRepository.save(table);
    }

    public RestaurantTable updateTable(Integer id, RestaurantTable tableDetails) {
        RestaurantTable table = getTableById(id);
        
        // Actualizar campos
        if (tableDetails.getTableNumber() != null) {
            // Verificar que el nuevo número no exista para otra mesa
            if (!table.getTableNumber().equals(tableDetails.getTableNumber()) && 
                restaurantTableRepository.existsByTableNumber(tableDetails.getTableNumber())) {
                throw new IllegalArgumentException("Ya existe una mesa con el número " + tableDetails.getTableNumber());
            }
            table.setTableNumber(tableDetails.getTableNumber());
        }
        
        if (tableDetails.getAbility() != null && tableDetails.getAbility() > 0) {
            table.setAbility(tableDetails.getAbility());
        }
        
        if (tableDetails.getState() != null) {
            table.setState(tableDetails.getState());
        }
        
        return restaurantTableRepository.save(table);
    }

    public void deleteTable(Integer id) {
        RestaurantTable table = getTableById(id);
        // Cambiar estado a inactivo en lugar de eliminar
        table.setState("I");
        restaurantTableRepository.save(table);
    }

    private Integer getNextTableId() {
        return restaurantTableRepository.findAll().stream()
                .map(RestaurantTable::getTableId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
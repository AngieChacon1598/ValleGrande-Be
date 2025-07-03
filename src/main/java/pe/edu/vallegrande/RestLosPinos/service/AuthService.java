package pe.edu.vallegrande.RestLosPinos.service;

import java.util.Optional;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.model.dto.RestaurantUserDTO;

public interface AuthService {
    String register(RestaurantUserDTO userDTO);
    Optional<String> login(String username, String password);
} 
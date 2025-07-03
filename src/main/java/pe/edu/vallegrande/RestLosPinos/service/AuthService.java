package pe.edu.vallegrande.RestLosPinos.service;

import java.util.Optional;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;

public interface AuthService {
    String register(RestaurantUser user);
    Optional<String> login(String username, String password);
} 
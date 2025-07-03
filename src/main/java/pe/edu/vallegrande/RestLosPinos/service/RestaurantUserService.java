package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.model.UserType;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantUserRepository;
import pe.edu.vallegrande.RestLosPinos.repository.UserTypeRepository;

import java.util.List;

@Service
@Transactional
public class RestaurantUserService {

    private final RestaurantUserRepository restaurantUserRepository;
    private final UserTypeRepository userTypeRepository;

    public RestaurantUserService(RestaurantUserRepository restaurantUserRepository, 
                               UserTypeRepository userTypeRepository) {
        this.restaurantUserRepository = restaurantUserRepository;
        this.userTypeRepository = userTypeRepository;
    }

    public RestaurantUser saveUser(RestaurantUser user) {
        // Validar que el tipo de usuario existe
        if (user.getUserType() == null || user.getUserType().getUserTypeId() == null) {
            throw new IllegalArgumentException("El tipo de usuario es obligatorio");
        }
        
        UserType userType = userTypeRepository.findById(user.getUserType().getUserTypeId())
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado"));
        
        // Establecer el estado por defecto como activo
        user.setState("A");
        
        // Validar campos obligatorios
        validateRequiredFields(user);
        
        // Validar formato de email
        validateEmail(user.getEmail());
        
        // Validar formato de teléfono
        validatePhone(user.getTelephone());
        
        // Validar que el username y email no existan
        validateUniqueFields(user);

        // Generar el siguiente ID
        Integer nextId = getNextUserId(); // Se vuelve a habilitar la generación manual
        user.setUserId(nextId); // Se vuelve a habilitar la asignación manual
        
        return restaurantUserRepository.save(user);
    }

    private void validateRequiredFields(RestaurantUser user) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (user.getNames() == null || user.getNames().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }
        if (user.getSurnames() == null || user.getSurnames().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }
        if (user.getDateOfBirth() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (user.getDocumentType() == null || user.getDocumentType().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }
        if (user.getNumberType() == null || user.getNumberType().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }
    }

    private void validateEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !phone.isEmpty() && !phone.matches("^[0-9]{9}$")) {
            throw new IllegalArgumentException("El formato del teléfono no es válido (debe tener 9 dígitos)");
        }
    }

    private void validateUniqueFields(RestaurantUser user) {
        // Validar username único
        if (restaurantUserRepository.existsByUserName(user.getUserName())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        // Validar email único
        if (restaurantUserRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
    }

    public RestaurantUser getUserById(Integer id) {
        return restaurantUserRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    public List<RestaurantUser> getAllActiveUsers() {
        return restaurantUserRepository.findByState("A");
    }

    public RestaurantUser updateUser(Integer id, RestaurantUser updatedUser) {
        RestaurantUser existingUser = getUserById(id);
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setNames(updatedUser.getNames());
        existingUser.setSurnames(updatedUser.getSurnames());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setTelephone(updatedUser.getTelephone());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDocumentType(updatedUser.getDocumentType());
        existingUser.setNumberType(updatedUser.getNumberType());
        existingUser.setUserType(updatedUser.getUserType());
        // No modificamos el estado para evitar cambios accidentales
        return restaurantUserRepository.save(existingUser);
    }

    public void deleteUserLogically(Integer id) {
        RestaurantUser user = getUserById(id);
        user.setState("I"); // 'I' = Inactivo
        restaurantUserRepository.save(user);
    }

    public void deleteUserPhysically(Integer id) {
        if (!restaurantUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario con ID " + id + " no existe");
        }
        restaurantUserRepository.deleteById(id);
    }

    public RestaurantUser restoreUser(Integer id) {
        RestaurantUser user = getUserById(id);
        if ("A".equals(user.getState())) {
            throw new IllegalStateException("El usuario con ID " + id + " ya está activo");
        }
        user.setState("A"); // 'A' = Activo
        return restaurantUserRepository.save(user);
    }

    // Se vuelve a habilitar el método getNextUserId()
    private Integer getNextUserId() {
        // Obtener el último ID de usuario
        List<RestaurantUser> users = restaurantUserRepository.findAll();
        if (users.isEmpty()) {
            return 1; // Si no hay usuarios, empezar con 1
        }
        
        // Encontrar el ID más alto y sumar 1
        return users.stream()
                .mapToInt(RestaurantUser::getUserId)
                .max()
                .orElse(0) + 1;
    }
}

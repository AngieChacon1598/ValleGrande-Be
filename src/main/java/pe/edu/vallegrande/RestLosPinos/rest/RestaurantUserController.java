package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.service.RestaurantUserService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class RestaurantUserController {

    private final RestaurantUserService userService;

    public RestaurantUserController(RestaurantUserService userService) {
        this.userService = userService;
    }

    // Crear nuevo usuario
    /*
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RestaurantUser user) {
        try {
            RestaurantUser savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el usuario: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantUser> getUserById(@PathVariable Integer id) {
        RestaurantUser user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Listar usuarios activos
    @GetMapping
    public ResponseEntity<List<RestaurantUser>> getAllActiveUsers() {
        List<RestaurantUser> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantUser> updateUser(@PathVariable Integer id, @RequestBody RestaurantUser user) {
        RestaurantUser updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminación lógica (cambiar estado a 'I')
    @DeleteMapping("/logical/{id}")
    public ResponseEntity<Void> deleteUserLogically(@PathVariable Integer id) {
        userService.deleteUserLogically(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminación física (borrar registro)
    @DeleteMapping("/physical/{id}")
    public ResponseEntity<Void> deleteUserPhysically(@PathVariable Integer id) {
        userService.deleteUserPhysically(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar usuario (cambiar estado a 'A')
    @PutMapping("/restore/{id}")
    public ResponseEntity<RestaurantUser> restoreUser(@PathVariable Integer id) {
        RestaurantUser restoredUser = userService.restoreUser(id);
        return ResponseEntity.ok(restoredUser);
    }
}

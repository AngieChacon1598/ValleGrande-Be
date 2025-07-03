package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.model.UserType;
import pe.edu.vallegrande.RestLosPinos.service.UserTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/user-types")
public class UserTypeController {

    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<List<UserType>> getAllUserTypes() {
        List<UserType> userTypes = userTypeService.getAllUserTypes();
        return ResponseEntity.ok(userTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> getUserTypeById(@PathVariable Integer id) {
        UserType userType = userTypeService.getUserTypeById(id);
        return ResponseEntity.ok(userType);
    }

    @PostMapping
    public ResponseEntity<UserType> createUserType(@RequestBody UserType userType) {
        UserType savedUserType = userTypeService.saveUserType(userType);
        return new ResponseEntity<>(savedUserType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserType> updateUserType(@PathVariable Integer id, @RequestBody UserType userType) {
        UserType updatedUserType = userTypeService.updateUserType(id, userType);
        return ResponseEntity.ok(updatedUserType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable Integer id) {
        userTypeService.deleteUserType(id);
        return ResponseEntity.noContent().build();
    }
} 
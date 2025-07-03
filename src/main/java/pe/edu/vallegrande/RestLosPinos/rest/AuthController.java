package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantUserRepository;
import pe.edu.vallegrande.RestLosPinos.service.AuthService;
import pe.edu.vallegrande.RestLosPinos.util.EncryptUtil;
import pe.edu.vallegrande.RestLosPinos.util.SecurityUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pe.edu.vallegrande.RestLosPinos.model.dto.RestaurantUserDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RestaurantUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RestaurantUserDTO userDTO) {
        return ResponseEntity.ok(authService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        return authService.login(req.get("username"), req.get("password"))
                .<ResponseEntity<?>>map(token -> ResponseEntity.ok(Map.of("token", token)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Integer userId = SecurityUtil.getCurrentUserId();
        String userRole = SecurityUtil.getCurrentUserRole();
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }
        
        return ResponseEntity.ok(Map.of(
            "userId", userId,
            "role", userRole
        ));
    }

    @PostMapping("/fix-user-data")
    public ResponseEntity<String> fixUserData() {
        try {
            List<RestaurantUser> users = userRepository.findAll();
            int fixedCount = 0;
            
            for (RestaurantUser user : users) {
                boolean needsUpdate = false;
                
                // Check if username is encrypted and needs to be decrypted
                if (user.getUserName() != null && user.getUserName().length() > 20) {
                    try {
                        String decryptedUsername = EncryptUtil.decrypt(user.getUserName());
                        user.setUserName(decryptedUsername);
                        needsUpdate = true;
                    } catch (Exception e) {
                        // Username is not encrypted, skip
                    }
                }
                
                // Check if password is not hashed and needs to be hashed
                if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    needsUpdate = true;
                }
                
                if (needsUpdate) {
                    userRepository.save(user);
                    fixedCount++;
                }
            }
            
            return ResponseEntity.ok("Datos de usuarios corregidos. " + fixedCount + " usuarios actualizados.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al corregir datos: " + e.getMessage());
        }
    }

    /*
    @GetMapping("/migrate-users")
    public ResponseEntity<String> migrateUsernames() {
        List<RestaurantUser> users = userRepository.findAll();
        for (RestaurantUser user : users) {
            try {
                // Si esto falla, significa que no está encriptado
                EncryptUtil.decrypt(user.getUserName());
            } catch (Exception e) {
                // Procedemos a encriptar
                user.setUserName(EncryptUtil.encrypt(user.getUserName()));
                userRepository.save(user);
            }
        }
        return ResponseEntity.ok("Migración de usernames completada.");
    }
    */
} 
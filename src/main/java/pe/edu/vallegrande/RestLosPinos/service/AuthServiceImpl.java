package pe.edu.vallegrande.RestLosPinos.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantUserRepository;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RestaurantUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    private final String SECRET = "myjwtsecret";

    @Override
    public String register(RestaurantUser userData) {
        RestaurantUser newUser = new RestaurantUser();
        newUser.setUserId(userData.getUserId());
        newUser.setUserName(userData.getUserName()); // Guardar en texto plano
        newUser.setPassword(encoder.encode(userData.getPassword())); // Hashear password
        newUser.setNames(userData.getNames());
        newUser.setSurnames(userData.getSurnames());
        newUser.setDateOfBirth(userData.getDateOfBirth());
        newUser.setAddress(userData.getAddress());
        newUser.setTelephone(userData.getTelephone());
        newUser.setEmail(userData.getEmail());
        newUser.setDocumentType(userData.getDocumentType());
        newUser.setNumberType(userData.getNumberType());
        newUser.setState(userData.getState());
        newUser.setUserType(userData.getUserType());
        userRepository.save(newUser);
        return "Registrado correctamente";
    }

    @Override
    public Optional<String> login(String username, String password) {
        Optional<RestaurantUser> userOptional = userRepository.findByUserName(username);
        if (userOptional.isPresent()) {
            RestaurantUser user = userOptional.get();
            if (encoder.matches(password, user.getPassword())) {
                String role = user.getUserType() != null ? user.getUserType().getName() : "USER";
                String token = Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .claim("userId", user.getUserId())
                        .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                        .compact();
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }
} 
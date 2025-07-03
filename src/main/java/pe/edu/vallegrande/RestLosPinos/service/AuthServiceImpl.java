package pe.edu.vallegrande.RestLosPinos.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.RestLosPinos.repository.RestaurantUserRepository;
import pe.edu.vallegrande.RestLosPinos.repository.UserTypeRepository;
import pe.edu.vallegrande.RestLosPinos.model.UserType;
import java.util.Optional;
import pe.edu.vallegrande.RestLosPinos.model.dto.RestaurantUserDTO;
import pe.edu.vallegrande.RestLosPinos.model.dto.UserTypeDTO;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RestaurantUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserTypeRepository userTypeRepository;
    private final String SECRET = "myjwtsecret";

    @Override
    public String register(RestaurantUserDTO userDTO) {
        RestaurantUser newUser = new RestaurantUser();
        newUser.setUserId(userDTO.getUserId());
        newUser.setUserName(userDTO.getUserName());
        newUser.setPassword(encoder.encode(userDTO.getPassword()));
        newUser.setNames(userDTO.getNames());
        newUser.setSurnames(userDTO.getSurnames());
        newUser.setDateOfBirth(userDTO.getDateOfBirth());
        newUser.setAddress(userDTO.getAddress());
        newUser.setTelephone(userDTO.getTelephone());
        newUser.setEmail(userDTO.getEmail());
        newUser.setDocumentType(userDTO.getDocumentType());
        newUser.setNumberType(userDTO.getNumberType());
        newUser.setState(userDTO.getState());
        Integer userTypeId = userDTO.getUserType() != null ? userDTO.getUserType().getId() : null;
        if (userTypeId == null) {
            throw new IllegalArgumentException("El campo userType.id es obligatorio");
        }
        UserType userType = userTypeRepository.findById(userTypeId)
            .orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));
        newUser.setUserType(userType);
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
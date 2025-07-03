package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.UserType;
import pe.edu.vallegrande.RestLosPinos.repository.UserTypeRepository;

import java.util.List;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public List<UserType> getAllUserTypes() {
        return userTypeRepository.findAll();
    }

    public UserType getUserTypeById(Integer id) {
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario con ID " + id + " no encontrado"));
    }

    public UserType saveUserType(UserType userType) {
        return userTypeRepository.save(userType);
    }

    public UserType updateUserType(Integer id, UserType userType) {
        UserType existingUserType = getUserTypeById(id);
        existingUserType.setName(userType.getName());
        return userTypeRepository.save(existingUserType);
    }

    public void deleteUserType(Integer id) {
        if (!userTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de usuario con ID " + id + " no existe");
        }
        userTypeRepository.deleteById(id);
    }
} 
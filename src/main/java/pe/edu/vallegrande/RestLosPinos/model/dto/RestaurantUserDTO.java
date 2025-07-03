package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class RestaurantUserDTO {
    private Integer userId;
    private String userName;
    private String password;
    private String names;
    private String surnames;
    private Date dateOfBirth;
    private String address;
    private String telephone;
    private String email;
    private String documentType;
    private String numberType;
    private String state;
    private UserTypeDTO userType;
}

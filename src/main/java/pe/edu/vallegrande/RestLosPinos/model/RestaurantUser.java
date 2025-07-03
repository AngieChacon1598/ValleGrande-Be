package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@Entity
@Table(name = "restaurant_user", schema = "DEVELOPER1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RestaurantUser {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Se elimina para generar ID manualmente
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", length = 220, nullable = false, unique = true)
    private String userName;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "names", length = 200, nullable = false)
    private String names;

    @Column(name = "surnames", length = 200, nullable = false)
    private String surnames;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "address", length = 150, nullable = false)
    private String address;

    @Column(name = "telephone", length = 9)
    private String telephone; // Validar formato en servicio o DTO

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "document_type", length = 25, nullable = false)
    private String documentType;

    @Column(name = "number_type", length = 200, nullable = false)
    private String numberType;

    @Column(name = "state", length = 1, nullable = false)
    private String state; // 'A' o 'I'

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

}

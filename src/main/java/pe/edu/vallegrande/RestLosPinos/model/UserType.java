package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_type", schema = "DEVELOPER1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserType {

    @Id
    @Column(name = "user_type_id")
    private Integer userTypeId;

    @Column(name = "name", length = 90, nullable = false)
    private String name;

}

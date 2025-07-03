package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "restaurant_table", schema = "DEVELOPER1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RestaurantTable {

    @Id
    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "table_number", nullable = false, unique = true)
    private Integer tableNumber;

    @Column(name = "ability", nullable = false)
    private Integer ability;

    @Column(name = "state", nullable = false, length = 1)
    private String state;
}
package pe.edu.vallegrande.RestLosPinos.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "order_status_type", schema = "DEVELOPER2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusType {

    @Id
    @Column(name = "id_type_state")
    private Integer idTypeState;

    @Column(name = "name", length = 90, nullable = false)
    private String name;
}

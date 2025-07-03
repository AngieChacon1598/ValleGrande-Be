package pe.edu.vallegrande.RestLosPinos.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "payment_type", schema = "DEVELOPER2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentType {

    @Id
    @Column(name = "id_payment_type")
    private Integer idPaymentType;

    @Column(name = "name", length = 90, nullable = false)
    private String name;
}

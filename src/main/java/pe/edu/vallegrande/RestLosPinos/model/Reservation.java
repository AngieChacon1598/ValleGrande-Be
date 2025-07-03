package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name = "reservation", schema = "DEVELOPER1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "reservation_name", length = 90, nullable = false)
    private String reservationName;

    @Column(name = "reservation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;

    @ManyToOne(fetch = FetchType.LAZY)  // Cambiar de EAGER a LAZY
    @JoinColumn(name = "id_type_state")
    private OrderStatusType orderStatusType;

    @ManyToOne(fetch = FetchType.LAZY)  // Este ya está como LAZY
    @JoinColumn(name = "user_id")
    private RestaurantUser restaurantUser;
}
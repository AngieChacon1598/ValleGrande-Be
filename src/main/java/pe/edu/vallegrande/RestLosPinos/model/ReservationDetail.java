package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "reservation_detail", schema = "DEVELOPER1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReservationDetail {

    @Id
    @Column(name = "id_reservation_detail")
    private Integer idReservationDetail;

    @Column(name = "number_people", nullable = false)
    private Integer numberPeople;

    @Column(name = "reservation_method", length = 50)
    private String reservationMethod;

    @Column(name = "request", length = 200)
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)  // Cambiar de EAGER a LAZY
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    @ManyToOne(fetch = FetchType.LAZY)  // Cambiar de EAGER a LAZY
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "state", nullable = false, length = 1)
    private String state = "A";  // A = Activo, I = Inactivo (eliminado lógicamente)
}
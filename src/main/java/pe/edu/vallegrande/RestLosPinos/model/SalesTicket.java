package pe.edu.vallegrande.RestLosPinos.model;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_ticket", schema = "DEVELOPER2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_ticket_seq")
    @SequenceGenerator(name = "sales_ticket_seq", sequenceName = "SALES_TICKET_SEQ", allocationSize = 1)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "total_payment", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Column(name = "delivery", length = 2)
    private String delivery;

    @Column(name = "delivery_address", length = 150)
    private String deliveryAddress;

    @Column(name = "note", length = 200)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RestaurantUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_state")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderStatusType orderStatusType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_type")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PaymentType paymentType;

    @OneToMany(mappedBy = "salesTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductDetail> productDetails = new ArrayList<>();
}

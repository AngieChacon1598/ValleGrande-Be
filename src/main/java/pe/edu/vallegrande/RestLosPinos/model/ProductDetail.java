package pe.edu.vallegrande.RestLosPinos.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "product_detail", schema = "DEVELOPER2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_detail_seq")
    @SequenceGenerator(name = "product_detail_seq", sequenceName = "SEQ_PRODUCT_DETAIL", allocationSize = 1)
    @Column(name = "id_detail_product")
    private Long idDetailProduct;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SalesTicket salesTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;
}

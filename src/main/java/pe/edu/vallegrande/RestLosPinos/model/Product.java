package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "product", schema = "DEVELOPER2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "status")
    private Boolean status;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructor sin createdAt
    public Product(String name, String description, BigDecimal price, Boolean status, String imageUrl, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Constructor con createdAt (para pruebas u otros usos)
    public Product(String name, String description, BigDecimal price, Boolean status, String imageUrl, Category category, LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.category = category;
        this.createdAt = createdAt;
    }
}

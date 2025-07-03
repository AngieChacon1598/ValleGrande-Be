package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "category", schema = "DEVELOPER2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID generado automáticamente
    private Long id;

    private String name;
    private String description; // Nueva descripción
    private String status; // 'A' = activo, 'I' = Eliminado lógicamente

    // Constructor personalizado para 2 campos usado en tests
    public Category(String name, String status) {
        this.name = name;
        this.status = status;
    }

    // Nuevo constructor personalizado para 3 campos
    public Category(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

}

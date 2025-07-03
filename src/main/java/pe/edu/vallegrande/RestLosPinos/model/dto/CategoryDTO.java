package pe.edu.vallegrande.RestLosPinos.model.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
} 
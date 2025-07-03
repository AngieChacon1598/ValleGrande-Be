package pe.edu.vallegrande.RestLosPinos.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.RestLosPinos.model.Category;
import pe.edu.vallegrande.RestLosPinos.model.dto.CategoryDTO;
import pe.edu.vallegrande.RestLosPinos.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Crear una categoría
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription(), categoryDTO.getStatus());
        Category savedCategory = categoryService.saveCategory(category);
        CategoryDTO savedCategoryDTO = new CategoryDTO();
        savedCategoryDTO.setId(savedCategory.getId());
        savedCategoryDTO.setName(savedCategory.getName());
        savedCategoryDTO.setDescription(savedCategory.getDescription());
        savedCategoryDTO.setStatus(savedCategory.getStatus());
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    // Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());
            dto.setStatus(category.getStatus());
            return dto;
        }).toList();
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    // Obtener una categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setStatus(category.getStatus());
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    // Actualizar una categoría
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Category updatedCategory = new Category();
        updatedCategory.setName(categoryDTO.getName());
        updatedCategory.setDescription(categoryDTO.getDescription());
        updatedCategory.setStatus(categoryDTO.getStatus());
        Category savedCategory = categoryService.updateCategory(id, updatedCategory);
        CategoryDTO savedCategoryDTO = new CategoryDTO();
        savedCategoryDTO.setId(savedCategory.getId());
        savedCategoryDTO.setName(savedCategory.getName());
        savedCategoryDTO.setDescription(savedCategory.getDescription());
        savedCategoryDTO.setStatus(savedCategory.getStatus());
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    // Eliminar una categoría (lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Eliminar una categoría (físico)
    @DeleteMapping("/physically/{id}")
    public ResponseEntity<Void> deleteCategoryPhysically(@PathVariable Long id) {
        categoryService.deleteCategoryPhysically(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Restaurar categoría eliminada lógicamente
    @PutMapping("/restore/{id}")
    public ResponseEntity<CategoryDTO> restoreCategory(@PathVariable Long id) {
        Category restoredCategory = categoryService.restoreCategory(id);
        CategoryDTO restoredCategoryDTO = new CategoryDTO();
        restoredCategoryDTO.setId(restoredCategory.getId());
        restoredCategoryDTO.setName(restoredCategory.getName());
        restoredCategoryDTO.setDescription(restoredCategory.getDescription());
        restoredCategoryDTO.setStatus(restoredCategory.getStatus());
        return new ResponseEntity<>(restoredCategoryDTO, HttpStatus.OK);
    }
}
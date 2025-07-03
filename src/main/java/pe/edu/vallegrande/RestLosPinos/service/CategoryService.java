package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.Category;
import pe.edu.vallegrande.RestLosPinos.model.dto.CategoryDTO;
import pe.edu.vallegrande.RestLosPinos.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + id + " no encontrada"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByStatus("A");
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = getCategoryById(id);
        
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());
        existingCategory.setStatus(updatedCategory.getStatus());
        
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        category.setStatus("E");
        categoryRepository.save(category);
    }

    // Eliminar categoría (físico)
    public void deleteCategoryPhysically(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría con ID " + id + " no existe.");
        }

        categoryRepository.deleteById(id);
    }

    // Restaurar categoría eliminada lógicamente
    public Category restoreCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + id + " no encontrada."));

        if ("A".equals(existingCategory.getStatus())) {
            throw new ResourceNotFoundException("La categoría con ID " + id + " ya está activa.");
        }

        existingCategory.setStatus("A");
        return categoryRepository.save(existingCategory);
    }
}
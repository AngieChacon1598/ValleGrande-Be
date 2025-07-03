package pe.edu.vallegrande.RestLosPinos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import pe.edu.vallegrande.RestLosPinos.model.Category;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.service.ProductService;
import pe.edu.vallegrande.RestLosPinos.rest.ProductController;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        LocalDateTime fechaActual = LocalDateTime.now();

        category = new Category();
        category.setId(1L);
        category.setName("Postres Fitness");
        category.setStatus("A");

        product = new Product(
            "Torta Proteica",
            "Deliciosa torta con proteína",
            BigDecimal.valueOf(25.50),  // Usar BigDecimal
            Boolean.TRUE,                // Usar Boolean objeto
            "https://url.de.la.imagen/torta_proteica.jpg",
            category,
            fechaActual
        );
        product.setId(1L);
    }

    @Test
    public void testCreateProduct() {
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        Product created = productController.createProduct(product);

        assertNotNull(created);
        assertEquals("Torta Proteica", created.getName());
        assertEquals(1L, created.getCategory().getId());
        verify(productService, times(1)).saveProduct(any(Product.class));
    }

    @Test
    public void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(product);

        Product found = productController.getProductById(1L);

        assertNotNull(found);
        assertEquals("Torta Proteica", found.getName());
        assertEquals("Postres Fitness", found.getCategory().getName());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productService.getAllProducts()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testUpdateProduct() {
        Product updatedProduct = new Product(
            "Torta Proteica V2",
            "Descripción mejorada",
            BigDecimal.valueOf(30.50),  // Usar BigDecimal
            Boolean.TRUE,               // Usar Boolean objeto
            "https://url.de.la.imagen/torta_proteica_v2.jpg",
            category,
            LocalDateTime.now()
        );
        updatedProduct.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        Product result = productController.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("Torta Proteica V2", result.getName());
        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        productController.deleteProduct(1L);

        verify(productService, times(1)).deleteProduct(1L);
    }
}

package pe.edu.vallegrande.RestLosPinos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.Category;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.repository.ProductRepository;
import pe.edu.vallegrande.RestLosPinos.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Postres Fitness");
        category.setStatus("A");

        product = new Product(
                "Torta Proteica",
                "Deliciosa torta con proteína",
                BigDecimal.valueOf(25.50),
                Boolean.TRUE,
                "https://url.de.la.imagen/torta_proteica.jpg",
                category,
                LocalDateTime.now()
        );
        product.setId(1L);
    }

    @Test
    public void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.saveProduct(product);

        assertNotNull(saved);
        assertEquals("Torta Proteica", saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Collections.singletonList(product);
        when(productRepository.findByStatus(Boolean.TRUE)).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByStatus(Boolean.TRUE);
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deleteProduct(1L);

        assertEquals(Boolean.FALSE, product.getStatus());
        verify(productRepository, times(1)).save(product);
    }
}

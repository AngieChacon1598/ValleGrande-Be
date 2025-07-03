package pe.edu.vallegrande.RestLosPinos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.exception.ResourceNotFoundException;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.model.Category;
import pe.edu.vallegrande.RestLosPinos.repository.ProductRepository;
import pe.edu.vallegrande.RestLosPinos.repository.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import java.io.InputStream;
import java.util.HashMap;
import java.io.File;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    public ProductService(ProductRepository productRepository, 
                         CategoryRepository categoryRepository,
                         JdbcTemplate jdbcTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product saveProduct(Product product) {
        product.setStatus(Boolean.TRUE); // por defecto activo al crear
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findByStatus(Boolean.TRUE); // solo activos
    }

    public List<Product> getInactiveProducts() {
        return productRepository.findByStatus(Boolean.FALSE); // productos inactivos
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setCategory(updatedProduct.getCategory());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus(Boolean.FALSE); // marca como eliminado (inactivo)
        productRepository.save(product);
    }

    public void deleteProductPhysically(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no existe");
        }
        productRepository.deleteById(id);
    }

    public Product restoreProduct(Long id) {
        Product product = getProductById(id);
        if (Boolean.TRUE.equals(product.getStatus())) {
            throw new ResourceNotFoundException("El producto con ID " + id + " ya está activo.");
        }

        // Actualizar solo el estado usando JDBC directo
        String sql = "UPDATE DEVELOPER2.product SET status = 1 WHERE id = ?";
        int updated = jdbcTemplate.update(sql, id);
        
        if (updated == 0) {
            throw new ResourceNotFoundException("No se pudo restaurar el producto con ID " + id);
        }

        // Recargar el producto actualizado
        return getProductById(id);
    }

    public byte[] generateJasperProductReportPdf(int estadoProducto) throws Exception {
        // 1. Cargar el archivo .jasper desde resources
        InputStream jasperStream = new ClassPathResource("reports/ListProducto.jasper").getInputStream();
        // 2. Crear un mapa de parámetros (si el reporte los necesita)
        HashMap<String, Object> params = new HashMap<>();
        params.put("ESTADO_PRODUCTO", String.valueOf(estadoProducto)); // 1=activo, 0=inactivo, siempre como String
        // Si el reporte tiene subreportes, se pasa la ruta:
        File subreportDir = new ClassPathResource("reports/").getFile();
        params.put("SUBREPORT_DIR", subreportDir.getAbsolutePath() + File.separator);
        // 3. Llenar el reporte con la conexión a la base de datos
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        // 4. Exportar el reporte a PDF y devolverlo como arreglo de bytes
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

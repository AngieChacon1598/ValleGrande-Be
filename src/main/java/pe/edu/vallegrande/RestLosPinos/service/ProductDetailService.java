package pe.edu.vallegrande.RestLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.RestLosPinos.model.ProductDetail;
import pe.edu.vallegrande.RestLosPinos.model.Product;
import pe.edu.vallegrande.RestLosPinos.model.SalesTicket;
import pe.edu.vallegrande.RestLosPinos.model.dto.ProductDetailDTO;
import pe.edu.vallegrande.RestLosPinos.repository.ProductDetailRepository;
import pe.edu.vallegrande.RestLosPinos.repository.ProductRepository;
import pe.edu.vallegrande.RestLosPinos.repository.SalesTicketRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final SalesTicketRepository salesTicketRepository;

    @Transactional
    public ProductDetailDTO createProductDetail(ProductDetailDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        SalesTicket salesTicket = salesTicketRepository.findById(dto.getSalesTicketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket de venta no encontrado"));

        ProductDetail productDetail = new ProductDetail();
        productDetail.setAmount(dto.getAmount());
        productDetail.setProduct(product);
        productDetail.setSalesTicket(salesTicket);

        productDetail = productDetailRepository.save(productDetail);
        return mapToDTO(productDetail);
    }

    public List<ProductDetailDTO> getAllProductDetails() {
        return productDetailRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductDetailDTO getProductDetailById(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de producto no encontrado"));
        return mapToDTO(productDetail);
    }

    @Transactional
    public ProductDetailDTO updateProductDetail(Long id, ProductDetailDTO dto) {
        ProductDetail existingDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de producto no encontrado"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        SalesTicket salesTicket = salesTicketRepository.findById(dto.getSalesTicketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket de venta no encontrado"));

        existingDetail.setAmount(dto.getAmount());
        existingDetail.setProduct(product);
        existingDetail.setSalesTicket(salesTicket);

        existingDetail = productDetailRepository.save(existingDetail);
        return mapToDTO(existingDetail);
    }

    @Transactional
    public void deleteProductDetail(Long id) {
        if (!productDetailRepository.existsById(id)) {
            throw new EntityNotFoundException("Detalle de producto no encontrado");
        }
        productDetailRepository.deleteById(id);
    }

    public List<ProductDetailDTO> getProductDetailsBySalesTicket(Long salesTicketId) {
        return productDetailRepository.findBySalesTicket_TicketId(salesTicketId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteProductDetailPhysically(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product detail not found"));
        productDetailRepository.delete(productDetail);
    }

    private ProductDetailDTO mapToDTO(ProductDetail productDetail) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(productDetail.getIdDetailProduct());
        dto.setAmount(productDetail.getAmount());
        if (productDetail.getProduct() != null) {
            dto.setProductId(productDetail.getProduct().getId());
            dto.setProductName(productDetail.getProduct().getName());
            dto.setProductPrice(productDetail.getProduct().getPrice());
        } else {
            dto.setProductId(null);
            dto.setProductName("Producto no disponible");
            dto.setProductPrice(null);
        }
        if (productDetail.getSalesTicket() != null) {
            dto.setSalesTicketId(productDetail.getSalesTicket().getTicketId());
        } else {
            dto.setSalesTicketId(null);
        }
        return dto;
    }
} 
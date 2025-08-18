package pe.edu.vallegrande.RestLosPinos.service;

import pe.edu.vallegrande.RestLosPinos.model.*;
import pe.edu.vallegrande.RestLosPinos.model.dto.*;
import pe.edu.vallegrande.RestLosPinos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import java.io.InputStream;
import java.util.HashMap;
import java.io.File;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal; // Importar BigDecimal
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesTicketRepository salesTicketRepository;
    private final ProductRepository productRepository;
    private final RestaurantUserRepository restaurantUserRepository;
    private final OrderStatusTypeRepository orderStatusTypeRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private DataSource dataSource;

    @Transactional
    public SalesResponseDTO createSale(SalesRequestDTO request) {
        // Agregar logging para debug
        System.out.println("=== DEBUG: Creando venta ===");
        System.out.println("Usuario ID: " + request.getUserId());
        System.out.println("Estado ID: " + request.getIdTypeState());
        System.out.println("Tipo de pago ID: " + request.getIdPaymentType());
        System.out.println("Total: " + request.getTotalPayment());
        System.out.println("Productos en la venta: " + request.getProductDetails().size());
        
        RestaurantUser user = restaurantUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        OrderStatusType statusType = orderStatusTypeRepository.findById(request.getIdTypeState())
                .orElseThrow(() -> new EntityNotFoundException("Estado de orden no encontrado"));

        PaymentType paymentType = paymentTypeRepository.findById(request.getIdPaymentType())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de pago no encontrado"));

        SalesTicket salesTicket = SalesTicket.builder()
                .saleDate(request.getSaleDate() != null ? request.getSaleDate() : LocalDateTime.now())
                .totalPayment(request.getTotalPayment())
                .delivery(request.getDelivery())
                .deliveryAddress(request.getDeliveryAddress())
                .note(request.getNote())
                .user(user)
                .orderStatusType(statusType)
                .paymentType(paymentType)
                .build();

        List<ProductDetail> details = new ArrayList<>();

        for (SalesDetailDTO pdDto : request.getProductDetails()) {
            // Agregar logging para debug
            System.out.println("Intentando buscar producto con ID: " + pdDto.getProductId());
            
            Product product = productRepository.findById(pdDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + pdDto.getProductId()));

            System.out.println("Producto encontrado: " + product.getName() + " (ID: " + product.getId() + ")");

            ProductDetail detail = ProductDetail.builder()
                    .amount(pdDto.getAmount())
                    .product(product)
                    .salesTicket(salesTicket)
                    .build();

            details.add(detail);
        }

        salesTicket.setProductDetails(details);

        salesTicket = salesTicketRepository.save(salesTicket);

        return mapToResponse(salesTicket);
    }

    public List<SalesResponseDTO> getAllSales() {
        List<SalesTicket> sales = salesTicketRepository.findAllActiveSales();
        return sales.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SalesResponseDTO> getSalesByUserId(Integer userId) {
        List<SalesTicket> sales = salesTicketRepository.findByUserId(userId);
        return sales.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SalesResponseDTO getSaleById(Long id) {
        try {
            SalesTicket salesTicket = salesTicketRepository.findByIdWithRelations(id)
                    .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
            
            // Forzar la inicialización de las colecciones lazy
            Hibernate.initialize(salesTicket.getProductDetails());
            if (salesTicket.getProductDetails() != null) {
                salesTicket.getProductDetails().forEach(detail -> {
                    Hibernate.initialize(detail.getProduct());
                });
            }
            
            return mapToResponse(salesTicket);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la venta: " + e.getMessage(), e);
        }
    }

    @Transactional
    public SalesResponseDTO updateSale(Long id, SalesRequestDTO request) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la venta no puede ser nulo");
        }

        SalesTicket existingSale = salesTicketRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));

        if (request.getUserId() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        RestaurantUser user = restaurantUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (request.getIdTypeState() == null) {
            throw new IllegalArgumentException("El ID del estado de orden no puede ser nulo");
        }
        OrderStatusType statusType = orderStatusTypeRepository.findById(request.getIdTypeState())
                .orElseThrow(() -> new EntityNotFoundException("Estado de orden no encontrado"));

        if (request.getIdPaymentType() == null) {
            throw new IllegalArgumentException("El ID del tipo de pago no puede ser nulo");
        }
        PaymentType paymentType = paymentTypeRepository.findById(request.getIdPaymentType())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de pago no encontrado"));

        // Actualizar los campos básicos
        existingSale.setSaleDate(request.getSaleDate() != null ? request.getSaleDate() : existingSale.getSaleDate());
        existingSale.setTotalPayment(request.getTotalPayment());
        existingSale.setDelivery(request.getDelivery());
        existingSale.setDeliveryAddress(request.getDeliveryAddress());
        existingSale.setNote(request.getNote());
        existingSale.setUser(user);
        existingSale.setOrderStatusType(statusType);
        existingSale.setPaymentType(paymentType);

        // Manejar los detalles del producto
        if (request.getProductDetails() != null) {
            // Eliminar los detalles existentes
            existingSale.getProductDetails().clear();
            
            // Agregar los nuevos detalles
            for (SalesDetailDTO pdDto : request.getProductDetails()) {
                if (pdDto.getProductId() == null) {
                    throw new IllegalArgumentException("El ID del producto no puede ser nulo");
                }
                Product product = productRepository.findById(pdDto.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + pdDto.getProductId()));

                ProductDetail detail = ProductDetail.builder()
                        .amount(pdDto.getAmount())
                        .product(product)
                        .salesTicket(existingSale)
                        .build();

                existingSale.getProductDetails().add(detail);
            }
        }

        existingSale = salesTicketRepository.save(existingSale);
        return mapToResponse(existingSale);
    }

    @Transactional
    public void deleteSale(Long id) {
        SalesTicket salesTicket = salesTicketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
        
        // Buscar el estado de orden "Inactivo" (ID 2)
        OrderStatusType inactiveStatus = orderStatusTypeRepository.findById(2) // Usa el ID 2 para 'Inactivo'
                .orElseThrow(() -> new EntityNotFoundException("Estado de orden 'Inactivo' no encontrado"));
        
        salesTicket.setOrderStatusType(inactiveStatus);
        salesTicketRepository.save(salesTicket);
    }

    @Transactional
    public void restoreSale(Long id) {
        try {
            SalesTicket salesTicket = salesTicketRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
            
            // Buscar el estado de orden "Activo" (ID 1)
            OrderStatusType activeStatus = orderStatusTypeRepository.findById(1) // Usa el ID 1 para 'Activo'
                    .orElseThrow(() -> new EntityNotFoundException("Estado de orden 'Activo' no encontrado"));
            
            salesTicket.setOrderStatusType(activeStatus);
            salesTicketRepository.save(salesTicket);
        } catch (EntityNotFoundException e) {
            throw e; // Relanzar si no se encuentra la venta o el estado
        } catch (Exception e) {
            // Registrar la excepción completa para depuración
            System.err.println("Error inesperado al restaurar la venta con ID: " + id + ": " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace completo
            throw new RuntimeException("Error interno al restaurar la venta: " + e.getMessage(), e);
        }
    }

    public List<SalesResponseDTO> getInactiveSales() {
        List<SalesTicket> sales = salesTicketRepository.findAllWithRelations().stream()
                .filter(sale -> sale.getOrderStatusType().getIdTypeState() == 2) // Filtrar por ID 2 (Inactivo)
                .collect(Collectors.toList());
        return sales.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSalePhysically(Long id) {
        SalesTicket salesTicket = salesTicketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
        
        // Primero eliminamos los detalles del producto para evitar problemas de clave foránea
        salesTicket.getProductDetails().clear();
        salesTicketRepository.save(salesTicket);
        
        // Luego eliminamos la venta
        salesTicketRepository.delete(salesTicket);
    }

    // Mapear entidad a DTO
    private SalesResponseDTO mapToResponse(SalesTicket ticket) {
        SalesResponseDTO response = new SalesResponseDTO();
        response.setTicketId(ticket.getTicketId());
        response.setSaleDate(ticket.getSaleDate());
        response.setTotalPayment(ticket.getTotalPayment());
        response.setDelivery(ticket.getDelivery());
        response.setDeliveryAddress(ticket.getDeliveryAddress());
        response.setNote(ticket.getNote());

        // Mapear el estado de la orden
        if (ticket.getOrderStatusType() != null) {
            OrderStatusTypeDTO statusTypeDTO = new OrderStatusTypeDTO();
            statusTypeDTO.setIdTypeState(ticket.getOrderStatusType().getIdTypeState());
            statusTypeDTO.setName(ticket.getOrderStatusType().getName());
            response.setOrderStatusType(statusTypeDTO);
        }

        // Mapear el tipo de pago
        if (ticket.getPaymentType() != null) {
            PaymentTypeDTO paymentTypeDTO = new PaymentTypeDTO();
            paymentTypeDTO.setIdPaymentType(ticket.getPaymentType().getIdPaymentType());
            paymentTypeDTO.setName(ticket.getPaymentType().getName());
            response.setPaymentType(paymentTypeDTO);
        }

        // Cargar el usuario
        RestaurantUser user = ticket.getUser();
        if (user != null) {
            try {
                RestaurantUserDTO userDto = new RestaurantUserDTO();
                userDto.setUserId(user.getUserId());
                userDto.setUserName(user.getUserName());
                userDto.setNames(user.getNames());
                userDto.setSurnames(user.getSurnames());
                userDto.setDateOfBirth(user.getDateOfBirth());
                userDto.setAddress(user.getAddress());
                userDto.setTelephone(user.getTelephone());
                userDto.setEmail(user.getEmail());
                userDto.setDocumentType(user.getDocumentType());
                userDto.setNumberType(user.getNumberType());
                userDto.setState(user.getState());
                
                // Cargar el tipo de usuario de manera segura
                try {
                    // Forzar la carga del tipo de usuario
                    Hibernate.initialize(user.getUserType());
                    UserType userType = user.getUserType();
                    if (userType != null) {
                        System.out.println("UserType encontrado - ID: " + userType.getUserTypeId() + ", Nombre: " + userType.getName());
                        UserTypeDTO userTypeDTO = new UserTypeDTO();
                        userTypeDTO.setId(userType.getUserTypeId());
                        userTypeDTO.setName(userType.getName());
                        userDto.setUserType(userTypeDTO);
                    } else {
                        System.out.println("UserType es null para el usuario: " + user.getUserId());
                        userDto.setUserType(null);
                    }
                } catch (Exception e) {
                    System.out.println("Error al cargar UserType para el usuario " + user.getUserId() + ": " + e.getMessage());
                    e.printStackTrace();
                    userDto.setUserType(null);
                }
                
                response.setUser(userDto);
            } catch (Exception e) {
                // Si hay algún error al cargar los datos del usuario, lo establecemos como null
                response.setUser(null);
            }
        }

        // Mapear los detalles del producto
        List<SalesDetailResponseDTO> detailResponses = ticket.getProductDetails().stream()
                .map(detail -> {
                    SalesDetailResponseDTO detailDto = new SalesDetailResponseDTO();
                    detailDto.setAmount(detail.getAmount());
                    
                    try {
                        Product product = detail.getProduct();
                        if (product != null) {
                            detailDto.setProductId(product.getId());
                            detailDto.setProductName(product.getName());
                            detailDto.setProductPrice(product.getPrice());
                        } else {
                            detailDto.setProductId(null);
                            detailDto.setProductName("Producto no disponible");
                            detailDto.setProductPrice(BigDecimal.ZERO);
                        }
                    } catch (Exception e) {
                        detailDto.setProductId(null);
                        detailDto.setProductName("Producto no disponible");
                        detailDto.setProductPrice(BigDecimal.ZERO);
                    }
                    
                    return detailDto;
                })
                .collect(Collectors.toList());

        response.setProductDetails(detailResponses);
        return response;
    }

    public byte[] generateJasperPdfReport() throws Exception {
        // Cargar archivo .jasper en src/main/resources/reports
        InputStream jasperStream = new ClassPathResource("reports/ListDate.jasper").getInputStream();
        // Agregar parámetro para subreportes con ruta absoluta
        HashMap<String, Object> params = new HashMap<>();
        File subreportDir = new ClassPathResource("reports/").getFile();
        params.put("SUBREPORT_DIR", subreportDir.getAbsolutePath() + File.separator);
        // Llenar reporte con conexión a Oracle Cloud
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] generateJasperPdfReportPorTicket(Long ticketId) throws Exception {
        InputStream jasperStream = new ClassPathResource("reports/ListDate.jasper").getInputStream();
        HashMap<String, Object> params = new HashMap<>();
        params.put("TICKET_ID_PRINCIPAL", java.math.BigDecimal.valueOf(ticketId));
        File subreportDir = new ClassPathResource("reports/").getFile();
        params.put("SUBREPORT_DIR", subreportDir.getAbsolutePath() + File.separator);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
} // <-- La llave de cierre de la clase debe estar aquí

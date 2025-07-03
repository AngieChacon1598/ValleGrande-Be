package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.RestLosPinos.model.ProductDetail;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    // Método para buscar detalles por ticketId
    List<ProductDetail> findBySalesTicket_TicketId(Long ticketId);
}

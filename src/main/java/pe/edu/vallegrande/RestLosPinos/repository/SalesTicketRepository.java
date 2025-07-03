package pe.edu.vallegrande.RestLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.vallegrande.RestLosPinos.model.SalesTicket;

import java.util.List;
import java.util.Optional;

public interface SalesTicketRepository extends JpaRepository<SalesTicket, Long> {
    // Aquí puedes agregar consultas personalizadas si las necesitas en el futuro
    
    @Query("SELECT DISTINCT s FROM SalesTicket s " +
           "LEFT JOIN FETCH s.user u " +
           "LEFT JOIN FETCH u.userType " +
           "LEFT JOIN FETCH s.orderStatusType " +
           "LEFT JOIN FETCH s.paymentType " +
           "LEFT JOIN FETCH s.productDetails pd " +
           "LEFT JOIN FETCH pd.product " +
           "WHERE s.orderStatusType.idTypeState != 2 AND s.orderStatusType.idTypeState != 4")
    List<SalesTicket> findAllActiveSales();

    @Query("SELECT DISTINCT s FROM SalesTicket s " +
           "LEFT JOIN FETCH s.user u " +
           "LEFT JOIN FETCH u.userType " +
           "LEFT JOIN FETCH s.orderStatusType " +
           "LEFT JOIN FETCH s.paymentType " +
           "LEFT JOIN FETCH s.productDetails pd " +
           "LEFT JOIN FETCH pd.product")
    List<SalesTicket> findAllWithRelations();

    @Query("SELECT DISTINCT s FROM SalesTicket s " +
           "LEFT JOIN FETCH s.user u " +
           "LEFT JOIN FETCH u.userType " +
           "LEFT JOIN FETCH s.orderStatusType " +
           "LEFT JOIN FETCH s.paymentType " +
           "LEFT JOIN FETCH s.productDetails pd " +
           "LEFT JOIN FETCH pd.product " +
           "WHERE s.ticketId = :id")
    Optional<SalesTicket> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM SalesTicket s " +
           "LEFT JOIN FETCH s.user u " +
           "LEFT JOIN FETCH u.userType " +
           "LEFT JOIN FETCH s.orderStatusType " +
           "LEFT JOIN FETCH s.paymentType " +
           "LEFT JOIN FETCH s.productDetails pd " +
           "LEFT JOIN FETCH pd.product " +
           "WHERE s.user.userId = :userId " +
           "AND s.orderStatusType.idTypeState != 2 AND s.orderStatusType.idTypeState != 4")
    List<SalesTicket> findByUserId(@Param("userId") Integer userId);
}

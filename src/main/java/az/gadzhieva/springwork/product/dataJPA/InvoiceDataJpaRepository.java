package az.gadzhieva.springwork.product.dataJPA;

import az.gadzhieva.springwork.product.model.Employee;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceDataJpaRepository extends JpaRepository<Invoice,Long> {
    List<Invoice> findByFirmIdAndUser(Long firmId, User user);
    List<Invoice> findByFirmAndUser(Firm firm, User user);
    List<Invoice> findAllByUser(User user);
    Optional<Invoice> findByIdAndUser(Long id, User user);

    List<Invoice> findByFirmId(Long id);
}

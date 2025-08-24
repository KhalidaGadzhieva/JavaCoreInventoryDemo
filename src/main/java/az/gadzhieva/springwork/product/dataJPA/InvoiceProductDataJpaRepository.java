package az.gadzhieva.springwork.product.dataJPA;

import az.gadzhieva.springwork.product.model.Employee;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import az.gadzhieva.springwork.product.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceProductDataJpaRepository extends JpaRepository<InvoiceProduct,Long> {
    List<InvoiceProduct> findAllByUser(User user);
    Optional<InvoiceProduct> findByIdAndUser(Long id, User user);
}

package az.gadzhieva.springwork.product.dataJPA;

import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDataJpaRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByBarcodeAndUser( Long barcode,User user);
    List<Product> findByFirmAndUser(Firm firm, User user);
    List<Product> findAllByUser(User user);
    Optional<Product> findByIdAndUser(Long id, User user);

    Optional<Product> findByBarcode(Long barcode);
   boolean existsByBarcodeAndUser(Long barcode, User user);
}

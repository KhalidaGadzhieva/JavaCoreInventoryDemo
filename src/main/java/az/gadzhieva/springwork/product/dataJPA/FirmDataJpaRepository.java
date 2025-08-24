package az.gadzhieva.springwork.product.dataJPA;

import az.gadzhieva.springwork.product.model.Employee;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirmDataJpaRepository extends JpaRepository<Firm,Long> {
    Optional<Firm> findByName(String name);
    List<Firm> findAllByUser(User user);
    Optional<Firm> findByIdAndUser(Long id, User user);
    Optional<Firm> findByNameAndUserId(String name, Long userId);

}

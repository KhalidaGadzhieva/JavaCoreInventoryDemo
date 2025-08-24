package az.gadzhieva.springwork.product.dataJPA;

import az.gadzhieva.springwork.product.model.Employee;
import az.gadzhieva.springwork.product.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeDataJpaRepository extends JpaRepository<Employee,Long> {
    List<Employee> findAllByUser(User user);
    Optional<Employee> findByIdAndUser(Long id, User user);

}

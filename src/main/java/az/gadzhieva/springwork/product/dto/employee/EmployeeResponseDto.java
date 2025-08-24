package az.gadzhieva.springwork.product.dto.employee;

import az.gadzhieva.springwork.product.model.Address;
import az.gadzhieva.springwork.product.model.Employee;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private LocalDate birthDate;
    private String passportNumber;
    private LocalDate hireDate;
    private String hireOrderNumber;
    private String email;
    private String phoneNumber;
    private String workplace;
    private String department;
    private Address address;
    private String notes;


}

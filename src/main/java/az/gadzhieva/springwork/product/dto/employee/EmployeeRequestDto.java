package az.gadzhieva.springwork.product.dto.employee;

import az.gadzhieva.springwork.product.model.Address;
import az.gadzhieva.springwork.product.model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmployeeRequestDto {

    private Long id;
    @NotBlank(message = "Firstname is required.")
    private String firstName;
    @NotBlank(message = "Lastname is required")
    private String lastName;
    @NotBlank(message = "Position is required.")
    private String position;
    @NotNull(message = "Birth date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotBlank(message = "Passport number is required.")
    private String passportNumber;
    @NotNull(message = "Hire date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @NotBlank(message = "Hire order number is required.")
    private String hireOrderNumber;
    @Email
    @NotBlank(message = "Email is required.")
    private String email;
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;
    @NotBlank(message = "Workplace is required.")
    private String workplace;
    @NotBlank(message = "Department is required.")
    private String department;
    @Valid
    private Address address;
    private String notes;

}

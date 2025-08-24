package az.gadzhieva.springwork.product.model;

import az.gadzhieva.springwork.product.security.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "address_country")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "address_house_number"))
    })
    private Address address;
    private String notes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

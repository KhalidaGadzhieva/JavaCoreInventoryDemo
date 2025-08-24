package az.gadzhieva.springwork.product.model;

import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.security.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String inn;
    private String mobileNumber;
    private String email;
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal outstandingDebt = BigDecimal.ZERO;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "address_country")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "address_house_number"))
    })
    private Address address;
    @JsonManagedReference(value = "firm-invoice")
    @Builder.Default
    @OneToMany(mappedBy = "firm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void setItems(List<Invoice> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = new ArrayList<>(items); // защищаемся от immutable списков
        }
    }


}

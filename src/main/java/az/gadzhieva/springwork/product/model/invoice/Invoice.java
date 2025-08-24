package az.gadzhieva.springwork.product.model.invoice;

import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.security.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @JsonBackReference(value = "firm-invoice")
    @ManyToOne
    @JoinColumn(name = "FIRM_ID", nullable = false)
    private Firm firm;
    private BigDecimal sum;
    private String note;
    @JsonManagedReference(value = "invoice-invoiceProduct")
    @Builder.Default
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceProduct> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void setItems(List<InvoiceProduct> items) {
        this.items.clear();
        this.items.addAll(items);
    }

}

package az.gadzhieva.springwork.product.model.invoice;

import az.gadzhieva.springwork.product.security.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class InvoiceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference(value = "invoice-invoiceProduct")
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    private Long productBarcode;
    private String productName;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Long quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}


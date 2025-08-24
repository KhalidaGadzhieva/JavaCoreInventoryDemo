package az.gadzhieva.springwork.product.model;

import az.gadzhieva.springwork.product.security.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long barcode;
    @ManyToOne
    @JoinColumn(name = "FIRM_ID", nullable = false)
    private Firm firm;
    @Enumerated(EnumType.STRING)
    private Category category;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal oldPurchasePrice;
    private BigDecimal oldSellingPrice;
    @Builder.Default
    @Column(nullable = false)
    private Long stock=0L;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




}

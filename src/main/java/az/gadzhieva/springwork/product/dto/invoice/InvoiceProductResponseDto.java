package az.gadzhieva.springwork.product.dto.invoice;

import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import lombok.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InvoiceProductResponseDto {
    private Long id;
    private Long invoiceId;
    private Long productBarcode;
    private String productName;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Long quantity;


}

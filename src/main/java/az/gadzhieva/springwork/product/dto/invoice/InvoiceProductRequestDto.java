package az.gadzhieva.springwork.product.dto.invoice;

import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class InvoiceProductRequestDto {
    private Long id;
    @NotNull(message = "Product barcode is required.")
    private Long productBarcode;
    @NotBlank(message = "Product name is required")

    private String productName;
    @NotNull(message = "Unit is required")
    private String unit;
    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private BigDecimal purchasePrice;
    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01", message = "Selling price must be positive")
    private BigDecimal sellingPrice;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;


}

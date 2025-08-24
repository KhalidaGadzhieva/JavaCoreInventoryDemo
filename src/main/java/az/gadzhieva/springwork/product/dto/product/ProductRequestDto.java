package az.gadzhieva.springwork.product.dto.product;

import az.gadzhieva.springwork.product.model.Category;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.Unit;
import az.gadzhieva.springwork.product.validation.product.UniqueProductBarcode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@UniqueProductBarcode(message = "This product barcode is already taken. Please use a unique name.")
public class ProductRequestDto {
    private Long id;
    @NotBlank(message = "Name is required.")
    private String name;
    @NotNull (message = "Barcode is required")
    private Long barcode;
    @NotNull (message = "Product's firm is required.")
    private Long  firmId;
    private Category category;
    @NotNull(message = "Unit is required.")
    private Unit unit;





}

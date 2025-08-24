package az.gadzhieva.springwork.product.dto.product;

import az.gadzhieva.springwork.product.model.Category;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.Unit;
import lombok.*;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductResponseDto {
    private Long id;
    private String name;
    private Long barcode;
    private Long firmId;
    private String firmName;
    private Category category;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Long stock;
    private Unit unit;





}

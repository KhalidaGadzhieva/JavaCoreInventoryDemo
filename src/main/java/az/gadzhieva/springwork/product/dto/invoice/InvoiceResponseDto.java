package az.gadzhieva.springwork.product.dto.invoice;

import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import lombok.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InvoiceResponseDto {
    private Long id;
    private LocalDate date;
    private Long firmId;
    private String firmName;
    private BigDecimal sum;
    private String note;
    private List<InvoiceProductResponseDto> items = new ArrayList<>();
}

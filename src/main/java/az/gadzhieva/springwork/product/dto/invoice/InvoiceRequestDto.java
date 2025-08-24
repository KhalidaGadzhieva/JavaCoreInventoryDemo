package az.gadzhieva.springwork.product.dto.invoice;

import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import az.gadzhieva.springwork.product.validation.invoice.NotEmptyInvoiceItems;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InvoiceRequestDto {
    private Long id;
    @NotNull(message = "Invoice date is required.")
    private LocalDate date;
    @NotNull(message = "Invoice firm is required.")
    private Long firmId;
    private String note;
    @Valid
    @NotEmptyInvoiceItems(message = "Invoice can not be empty.")
    private List<InvoiceProductRequestDto> items = new ArrayList<>();
}

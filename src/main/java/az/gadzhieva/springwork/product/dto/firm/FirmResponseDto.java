package az.gadzhieva.springwork.product.dto.firm;

import az.gadzhieva.springwork.product.dto.invoice.InvoiceResponseDto;
import az.gadzhieva.springwork.product.model.Address;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FirmResponseDto {
    private Long id;
    private String name;
    private String inn;
    private String email;
    private String mobileNumber;
    private Address address;
    private BigDecimal outstandingDebt;
    private List<InvoiceResponseDto> items = new ArrayList<>();


}

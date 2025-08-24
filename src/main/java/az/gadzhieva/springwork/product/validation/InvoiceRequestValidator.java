package az.gadzhieva.springwork.product.validation;

import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InvoiceRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return InvoiceRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InvoiceRequestDto dto = (InvoiceRequestDto) target;

        // Проверяем, что items не пустой и в нем есть хотя бы один заполненный элемент
        if (dto.getItems() == null || dto.getItems().stream()
                .allMatch(item -> isEmptyItem(item))) {
            errors.rejectValue("items", "NotEmpty", "Invoice can not empty.");
        }
    }

    private boolean isEmptyItem(InvoiceProductRequestDto item) {
        return (item.getProductBarcode() == null)
                && (item.getProductName() == null || item.getProductName().isBlank())
                && item.getPurchasePrice() == null
                && item.getSellingPrice() == null
                && item.getQuantity() == null;
    }
}

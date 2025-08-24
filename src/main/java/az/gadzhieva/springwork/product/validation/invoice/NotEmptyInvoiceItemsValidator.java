package az.gadzhieva.springwork.product.validation.invoice;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyInvoiceItemsValidator implements ConstraintValidator<NotEmptyInvoiceItems, List<InvoiceProductRequestDto>> {

    @Override
    public void initialize(NotEmptyInvoiceItems constraintAnnotation) {
        // можно ничего не делать
    }

    @Override
    public boolean isValid(List<InvoiceProductRequestDto> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            return false; // список пуст — ошибка
        }
        // Проверяем, что в списке есть хотя бы один не пустой элемент
        return items.stream().anyMatch(item -> !isEmptyItem(item));
    }

    private boolean isEmptyItem(InvoiceProductRequestDto item) {
        if (item == null) return true;
        return (item.getProductBarcode() == null)
                && (item.getProductName() == null || item.getProductName().isBlank())
                && item.getPurchasePrice() == null
                && item.getSellingPrice() == null
                && item.getQuantity() == null;
    }
}

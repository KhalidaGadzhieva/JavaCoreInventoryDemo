package az.gadzhieva.springwork.product.error;

public class InvoiceProductNotFoundException extends RuntimeException {
    public InvoiceProductNotFoundException(String message) {
        super(message);
    }
}

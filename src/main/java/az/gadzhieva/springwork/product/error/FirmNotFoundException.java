package az.gadzhieva.springwork.product.error;

public class FirmNotFoundException extends RuntimeException {
    public FirmNotFoundException(String message) {
        super(message);
    }
}

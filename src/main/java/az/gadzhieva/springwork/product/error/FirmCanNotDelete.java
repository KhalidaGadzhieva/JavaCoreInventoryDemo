package az.gadzhieva.springwork.product.error;

public class FirmCanNotDelete extends RuntimeException {
    public FirmCanNotDelete(String message) {
        super(message);
    }
}

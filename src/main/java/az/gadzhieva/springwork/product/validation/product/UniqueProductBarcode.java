package az.gadzhieva.springwork.product.validation.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueProductBarcodeValidator.class)  // Класс валидатора — следующий шаг
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProductBarcode {
    String message() default "This product barcode is already taken. Please choose another one.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


package az.gadzhieva.springwork.product.validation.invoice;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyInvoiceItemsValidator.class) // класс валидатора, который напишем ниже
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyInvoiceItems {
    String message() default "Invoice can not be empty.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

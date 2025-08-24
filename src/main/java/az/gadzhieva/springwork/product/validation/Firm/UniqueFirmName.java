package az.gadzhieva.springwork.product.validation.Firm;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueFirmNameValidator.class)  // Класс валидатора — следующий шаг
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueFirmName {
    String message() default "This firm name is already taken. Please choose another one.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

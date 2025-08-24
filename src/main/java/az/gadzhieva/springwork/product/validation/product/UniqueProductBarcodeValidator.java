package az.gadzhieva.springwork.product.validation.product;

import az.gadzhieva.springwork.product.dataJPA.ProductDataJpaRepository;
import az.gadzhieva.springwork.product.dto.product.ProductRequestDto;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueProductBarcodeValidator implements ConstraintValidator<UniqueProductBarcode, ProductRequestDto> {
    @Autowired
    private ProductDataJpaRepository productDataJpaRepository;
    @Autowired
    private  UserProvider userProvider;


    @Override
    public boolean isValid(ProductRequestDto productRequestDto, ConstraintValidatorContext context) {
        User user = userProvider.getCurrentUser();
        Optional<Product> existingProduct = productDataJpaRepository.findByBarcodeAndUser(productRequestDto.getBarcode(), user);

        boolean valid = existingProduct
                .map(f -> f.getId().equals(productRequestDto.getId()))
                .orElse(true);

        if (!valid) {
            // Отключаем стандартное сообщение об ошибке (которое вешается на класс)
            context.disableDefaultConstraintViolation();

            // Добавляем ошибку на конкретное поле "name"
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("barcode")
                    .addConstraintViolation();
        }

        return valid;
    }

}
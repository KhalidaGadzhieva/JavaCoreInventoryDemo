package az.gadzhieva.springwork.product.validation.Firm;

import az.gadzhieva.springwork.product.dataJPA.FirmDataJpaRepository;
import az.gadzhieva.springwork.product.dto.firm.FirmRequestDto;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueFirmNameValidator implements ConstraintValidator<UniqueFirmName, FirmRequestDto> {
    @Autowired
    private FirmDataJpaRepository firmRepository;
    @Autowired
    private UserProvider userProvider;

    @Override
    public boolean isValid(FirmRequestDto firmRequestDto, ConstraintValidatorContext context) {
        Long currentUserId = userProvider.getCurrentUser().getId();

        Optional<Firm> existingFirm = firmRepository.findByNameAndUserId(firmRequestDto.getName(),currentUserId);

        boolean valid = existingFirm
                .map(f -> f.getId().equals(firmRequestDto.getId()))
                .orElse(true);

        if (!valid) {
            // Отключаем стандартное сообщение об ошибке (которое вешается на класс)
            context.disableDefaultConstraintViolation();

            // Добавляем ошибку на конкретное поле "name"
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("name")
                    .addConstraintViolation();
        }

        return valid;
    }

}

package az.gadzhieva.springwork.product.dto.firm;

import az.gadzhieva.springwork.product.model.Address;
import az.gadzhieva.springwork.product.validation.Firm.UniqueFirmName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@UniqueFirmName(message = "This firm name is already taken. Please use a unique name.")
public class FirmRequestDto {

    private Long id;

    @NotBlank(message = "Firm name must not be null.")

    private String name;

    @NotBlank(message = "INN is required and cannot be blank.")
    private String inn;

    @Valid
    private Address address;

    @NotBlank(message = "Mobile number is required and cannot be blank.")
    private String mobileNumber;

    @Email(message = "Email format is invalid.")
    @NotBlank(message = "Email is required and cannot be blank.")
    private String email;



}

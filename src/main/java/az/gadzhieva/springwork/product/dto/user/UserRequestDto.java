package az.gadzhieva.springwork.product.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    @NotBlank(message = "Username is required.")
    private String username;
    @NotBlank(message="Password is required")
    private String password;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email's format is not valid.")
    private String email;
}

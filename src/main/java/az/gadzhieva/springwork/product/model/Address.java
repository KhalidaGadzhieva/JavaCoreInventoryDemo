package az.gadzhieva.springwork.product.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Embeddable
public class Address {
    @NotBlank(message = "Country is required and cannot be blank.")
    private String country;
    @NotBlank(message = "City is required and cannot be blank.")
    private String city;
    @NotBlank(message = "Street is required and cannot be blank.")
    private String street;
    private String houseNumber;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) &&
                Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(houseNumber, address.houseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNumber);
    }
}

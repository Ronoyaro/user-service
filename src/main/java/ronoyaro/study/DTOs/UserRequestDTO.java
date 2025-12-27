package ronoyaro.study.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is invalid")
    private String email;
}

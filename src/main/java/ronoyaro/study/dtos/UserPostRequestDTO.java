package ronoyaro.study.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserPostRequestDTO {
    @NotBlank(message = "The field 'firstName' is required")
    @Schema(description = "user's first name",example = "Goku")
    private String firstName;
    @NotBlank(message = "The field 'lastName' is required")
    @Schema(description = "user's last name",example = "Son")
    private String lastName;
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "The email is invalid")
    @Schema(description = "user's email. Must be unique",example = "SonGoku@example.com")
    private String email;
}

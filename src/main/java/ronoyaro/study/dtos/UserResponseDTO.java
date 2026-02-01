package ronoyaro.study.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(description = "user's first name",example = "Goku")
    private String firstName;
    @Schema(description = "user's last name",example = "Son")
    private String lastName;
    @Schema(description = "user's email",example = "SonGoku@example.com")
    private String email;
}

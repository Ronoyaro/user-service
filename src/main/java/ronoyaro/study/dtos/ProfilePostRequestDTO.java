package ronoyaro.study.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfilePostRequestDTO {
    @NotBlank(message = "The field 'name' is required")
    private String name;
    @NotBlank(message = "The field 'description' is required")
    private String description;
}

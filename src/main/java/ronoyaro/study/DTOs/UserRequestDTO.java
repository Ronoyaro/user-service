package ronoyaro.study.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
}

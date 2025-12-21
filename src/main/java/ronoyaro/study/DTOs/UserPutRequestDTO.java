package ronoyaro.study.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPutRequestDTO {
    private Long id;
    private String email;
}

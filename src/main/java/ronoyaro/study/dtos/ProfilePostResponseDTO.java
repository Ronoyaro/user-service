package ronoyaro.study.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfilePostResponseDTO {
    private Long id;
    private String name;
    private String description;
}

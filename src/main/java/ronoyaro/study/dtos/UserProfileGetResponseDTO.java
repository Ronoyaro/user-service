package ronoyaro.study.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * public record UserProfileGetResponseDTO(
 *     Long id,
 *     User user,
 *     Profile profile
 * ) {
 *     public record User(Long id, String firstName) {}
 *     public record Profile(Long id, String name) {}
 * }
 * */

@Getter
@Setter
@ToString
@Builder

public class UserProfileGetResponseDTO {
    public record User(Long id, String firstName) {
    }


    public record Profile(Long id, String name) {
    }

    private Long id;
    private User user;
    private Profile profile;


}

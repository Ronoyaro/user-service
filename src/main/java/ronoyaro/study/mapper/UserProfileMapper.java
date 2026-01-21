package ronoyaro.study.mapper;

import org.mapstruct.Mapper;
import ronoyaro.study.domain.User;
import ronoyaro.study.domain.UserProfile;
import ronoyaro.study.dtos.UserProfileGetResponseDTO;
import ronoyaro.study.dtos.UserResponseDTO;

import java.util.List;

@Mapper
public interface UserProfileMapper {
    List<UserProfileGetResponseDTO> toUserProfileGetResponseDTO(List<UserProfile> userProfiles);
    List<UserResponseDTO> toUserResponseDTO(List<User> users);
}

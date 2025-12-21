package ronoyaro.study.mapper;

import org.mapstruct.Mapper;
import ronoyaro.study.DTOs.UserPutRequestDTO;
import ronoyaro.study.DTOs.UserRequestDTO;
import ronoyaro.study.DTOs.UserResponseDTO;
import ronoyaro.study.domain.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserResponseDTO> toUsersListResponseDTO(List<User> users);

    UserResponseDTO toUserResponseDTO(User user);

    User toUser(UserRequestDTO userRequestDTO);

    User toUser(UserPutRequestDTO userPutRequestDTO);
}

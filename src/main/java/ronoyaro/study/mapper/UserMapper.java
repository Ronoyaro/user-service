package ronoyaro.study.mapper;

import org.mapstruct.Mapper;
import ronoyaro.study.domain.User;
import ronoyaro.study.dtos.UserPostRequestDTO;
import ronoyaro.study.dtos.UserPutRequestDTO;
import ronoyaro.study.dtos.UserResponseDTO;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserResponseDTO> toUsersListResponseDTO(List<User> users);

    UserResponseDTO toUserResponseDTO(User user);

    User toUser(UserPostRequestDTO userPostRequestDTO);

    User toUser(UserPutRequestDTO userPutRequestDTO);
}

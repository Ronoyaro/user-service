package ronoyaro.study.mapper;

import org.mapstruct.Mapper;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.dtos.ProfileGetResponseDTO;
import ronoyaro.study.dtos.ProfilePostRequestDTO;

import java.util.List;

@Mapper
public interface ProfileMapper {
    Profile toProfile(ProfilePostRequestDTO profilePostRequestDTO);

    ProfileGetResponseDTO toProfileGetResponse(Profile profile);

    List<ProfileGetResponseDTO> toProfilesGetResponse(List<Profile> profile);
}

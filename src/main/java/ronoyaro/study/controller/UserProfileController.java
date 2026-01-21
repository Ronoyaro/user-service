package ronoyaro.study.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ronoyaro.study.domain.UserProfile;
import ronoyaro.study.dtos.UserProfileGetResponseDTO;
import ronoyaro.study.dtos.UserResponseDTO;
import ronoyaro.study.mapper.UserProfileMapper;
import ronoyaro.study.service.UserProfileService;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponseDTO>> findAll() {
        log.debug("Request received to list all user-profiles");

        List<UserProfile> profiles = service.findAll();
        var profilesResponse = mapper.toUserProfileGetResponseDTO(profiles);

        return ResponseEntity.ok(profilesResponse);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserResponseDTO>> findAllUsersByProfileId(@PathVariable Long id) {

        log.debug("Request received to list all users by profile id '{}'", id);

        var users = service.findAllUsersByProfileId(id);

        var usersResponse = mapper.toUserResponseDTO(users);

        return ResponseEntity.ok(usersResponse);
    }
}

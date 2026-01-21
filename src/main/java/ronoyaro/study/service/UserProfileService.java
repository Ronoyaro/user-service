package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ronoyaro.study.domain.User;
import ronoyaro.study.domain.UserProfile;
import ronoyaro.study.repository.UserProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    public List<User> findAllUsersByProfileId(Long profileId) {
        return repository.findAllUsersByProfileId(profileId);
    }
}

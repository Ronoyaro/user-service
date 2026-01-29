package ronoyaro.study.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ronoyaro.study.domain.UserProfile;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {
    private final UserUtils userUtils;
    private final ProfileUtils profileUtils;

    public List<UserProfile> newProfileList() {
        var userProfile = newUserProfileSaved();
        return Collections.singletonList(userProfile);
    }

    public UserProfile newUserProfileToSave() {
        return UserProfile.builder()
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }

    public UserProfile newUserProfileSaved() {
        return UserProfile.builder()
                .id(1L)
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }
}

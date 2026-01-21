package ronoyaro.study.utils;

import org.springframework.stereotype.Component;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.dtos.ProfilePostRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtils {
    private final List<Profile> profiles = new ArrayList<>();

    public ProfileUtils() {
        profiles.add(new Profile(1L, "Admin", "user created to admin test"));
        profiles.add(new Profile(2L, "User", "user created to user test"));
        profiles.add(new Profile(3L, "Guest", "user created to guest test"));
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public Profile newProfile() {
        return new Profile(1L, "Guest 2", "user created to guest 2 test");
    }

    public ProfilePostRequestDTO newProfileBlank() {
        return new ProfilePostRequestDTO();
    }

}

package ronoyaro.study.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ronoyaro.study.domain.UserProfile;
import ronoyaro.study.repository.UserProfileRepository;
import ronoyaro.study.utils.ProfileUtils;
import ronoyaro.study.utils.UserProfileUtils;
import ronoyaro.study.utils.UserUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService service;
    @Mock
    private UserProfileRepository repository;
    private List<UserProfile> userProfileList;
    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Spy //Preciso do objeto real, caso nao, tenho erro de nullPointerException
    private UserUtils userUtils;
    @Spy //Spy pq estamos no contexto do Mockito
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newProfileList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all user profiles")
    void findAll_ReturnsAListWithAllUsersProfiles() {

        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles)
                .isNotNull()
                .hasSameElementsAs(userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile)
                .hasNoNullFieldsOrProperties());

    }

    @Test
    @Order(2)
    @DisplayName("findAllUsersByProfileId returns a list with all users for a given profile")
    void findAllUsersByProfileId_ReturnsAllUsersWithProfileIdGiven() {

        Long profileId = 1L;

        var usersByProfile = userProfileList.stream()
                .filter(userProfile -> userProfile.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersByProfile);

        var users = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users)
                .hasSize(1)
                .doesNotContainNull();

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());

    }


}
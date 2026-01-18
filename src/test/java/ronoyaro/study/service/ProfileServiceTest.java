package ronoyaro.study.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.repository.ProfileRepository;
import ronoyaro.study.utils.ProfileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;
    @InjectMocks
    private ProfileUtils profileUtils;
    @Mock
    private ProfileRepository repository;
    List<Profile> profilesList = new ArrayList<>();

    @BeforeEach
    void init() {
        profilesList = profileUtils.getProfiles();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all Profiles when the argument param is null")
    void findAll_returnsAllProfiles_WhenSuccessful() {

        BDDMockito.when(repository.findAll()).thenReturn(profilesList);

        Assertions.assertThat(service.findAll(null))
                .isNotEmpty()
                .hasSameElementsAs(profilesList);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns a list with all Profiles founds when argument exists")
    void findAll_returnsAllProfilesFound_WhenSuccessful() {

        var profileExpected = profilesList.getFirst();

        BDDMockito.when(repository.findByName(profileExpected.getName()))
                .thenReturn(Collections.singletonList(profileExpected));

        Assertions.assertThat(service.findAll(profileExpected.getName()))
                .isNotEmpty()
                .contains(profileExpected);
    }

    @Test
    @Order(3)
    @DisplayName("findById returns a profile found when the id given exists")
    void findById_ReturnsAProfile_whenSuccessful() {
        var profileExpected = profilesList.getFirst();

        BDDMockito.when(repository.findById(profileExpected.getId())).thenReturn(Optional.of(profileExpected));

        Assertions.assertThat(service.findById(profileExpected.getId()))
                .isEqualTo(profileExpected);

    }

    @Test
    @Order(4)
    @DisplayName("findById throws NotFoundException when the Profile is not found")
    void findById_ThrowsException_WhenProfileIsNotFound() {
        var profileNotExpected = profilesList.getLast();

        BDDMockito.when(repository.findById(profileNotExpected.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(profileNotExpected.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(5)
    @DisplayName("save creates a new Profile when successful")
    void save_CreatesANewProfile_whenSuccessful() {
        var profileToSave = profileUtils.newProfile();

        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileToSave);

        Assertions.assertThat(service.save(profileToSave))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(profileToSave);

    }


}
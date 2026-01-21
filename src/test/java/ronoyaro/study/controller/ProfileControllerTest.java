package ronoyaro.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.dtos.ProfilePostRequestDTO;
import ronoyaro.study.exception.ErrorMessageDefault;
import ronoyaro.study.mapper.ProfileMapperImpl;
import ronoyaro.study.repository.ProfileRepository;
import ronoyaro.study.service.ProfileService;
import ronoyaro.study.utils.FileUtils;
import ronoyaro.study.utils.ProfileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProfileRepository.class, ProfileService.class, ProfileMapperImpl.class, ProfileUtils.class, FileUtils.class})
@Slf4j
class ProfileControllerTest {
    private static final String URL = "/v1/profiles";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProfileUtils profileUtils;

    @MockBean
    private ProfileRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList = profileUtils.getProfiles();
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/profiles returns all profiles when param argument is null")
    void findAll_ReturnsAllProfiles_WhenTheNameArgumentIsNull() throws Exception {
        var response = fileUtils.readResourceLoader("profile/get-find-all-request-200.json");

        when(repository.findAll()).thenReturn(profileList);

        mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/profiles?name=Admin returns a profile when param name is satisfied")
    void findByName_ReturnsAProfileWhen_NameArgumentIsSatisfied() throws Exception {

        String response = fileUtils.readResourceLoader("profile/get-find-by-name-argument-admin-200.json");

        String name = "Admin";

        var profileExpected = profileUtils.getProfiles()
                .stream()
                .filter(profile -> profile.getName().equalsIgnoreCase(name))
                .toList();

        when(repository.findByName(name)).thenReturn(profileExpected);

        mockMvc.perform(get(URL).param("name", name))
                .andDo(print())
                .andExpect(content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/profiles?name=Admin returns a profile when param name is satisfied")
    void findByName_ReturnsAProfileWhen_NameArgumentIsSatisfied2() throws Exception {

        String name = "Admin";

        var profileExpected = profileUtils.getProfiles()
                .stream()
                .filter(profile -> profile.getName().equalsIgnoreCase(name))
                .toList();

        var response = objectMapper.writeValueAsString(profileExpected);

        when(repository.findByName(name)).thenReturn(profileExpected);

        mockMvc.perform(get(URL).param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));

    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/profiles?name=xaxa returns an empty list when can't find any profile")
    void findByName_ReturnsAnEmptyList() throws Exception {

        String name = "xaxa";

        var emptyListExpected = profileUtils.getProfiles()
                .stream()
                .filter(profile -> profile.getName().equalsIgnoreCase(name))
                .toList();

        var response = objectMapper.writeValueAsString(emptyListExpected);

        when(repository.findByName(name)).thenReturn(emptyListExpected);

        mockMvc.perform(get(URL).param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));

    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/profiles/{id} returns a profile when successful")
    void findById_ReturnsAProfile_whenSuccessful() throws Exception {

        Long id = 1L;

        var profileFound = profileUtils.getProfiles()
                .stream()
                .filter(profile -> profile.getId().equals(id))
                .findFirst();

        var response = objectMapper.writeValueAsString(profileFound);

        when(repository.findById(id)).thenReturn(profileFound);

        mockMvc.perform(get(URL.concat("/{id}"), id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));


    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/profiles/{id} throws NotFoundException when Profile is not found")
    void findById_Throws_whenProfileIsNotFound() throws Exception {

        Long id = 99L;
        var profileNotFound = new ErrorMessageDefault(404, "Profile not found");

        var response = objectMapper.writeValueAsString(profileNotFound);

        when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(URL.concat("/{id}"), id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));

    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/profiles saves a new Profile")
    void save_CreatesANewProfile_whenSucessful() throws Exception {

        var postProfile = new ProfilePostRequestDTO();
        postProfile.setName("Guest 3");
        postProfile.setDescription("user created to guest test");

        var request = objectMapper.writeValueAsString(postProfile);

        var getProfile = new Profile(1L, "Guest 3", "user created to guest test");
        var response = objectMapper.writeValueAsString(getProfile);

        when(repository.save(any(Profile.class))).thenReturn(getProfile);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(response));

    }

    @Test
    @Order(7)
    @DisplayName("POST /v1/profiles throws Bad Request when fields aren't valids")
    void save_ThrowsBadRequest_WhenFieldIsBlank() throws Exception {

        var profileBlank = profileUtils.newProfileBlank();
        profileBlank.setName("");
        profileBlank.setDescription("");

        var request = objectMapper.writeValueAsString(profileBlank);

        var fieldsRequiredError = List.of("The field 'name' is required", "The field 'description' is required");

        var result = mockMvc.perform(post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();

        Assertions.assertThat(exception.getMessage()).contains(fieldsRequiredError);
    }


}
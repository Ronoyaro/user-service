package ronoyaro.study.controller;

import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.mapper.ProfileMapperImpl;
import ronoyaro.study.repository.ProfileRepository;
import ronoyaro.study.service.ProfileService;
import ronoyaro.study.utils.FileUtils;
import ronoyaro.study.utils.ProfileUtils;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProfileRepository.class, ProfileService.class, ProfileMapperImpl.class, ProfileUtils.class, FileUtils.class})
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

    List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList = profileUtils.getProfiles();
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/profiles returns all profiles when the param argument is null")
    void findAll_ReturnsAllProfiles_WhenTheNameArgumentIsNull() throws Exception {
        var request = fileUtils.readResourceLoader("profile/get-find-all-request-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(request));

    }


}
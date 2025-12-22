package ronoyaro.study.controller;


import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ronoyaro.study.domain.User;
import ronoyaro.study.repository.UserData;
import ronoyaro.study.repository.UserRepository;
import ronoyaro.study.utils.FileUtils;
import ronoyaro.study.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = UserController.class)
@ComponentScan(basePackages = "ronoyaro.study")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserData userData;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    @SpyBean
    private UserRepository repository;

    List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = userUtils.getUsersList();
    }

    @Test
    @Order(1)
    @DisplayName("/v1/users returns a list with all users")
    void findAll_ReturnsAnUserWithAllUsers_WhenSuccessful() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var usersResponse = fileUtils.readResourceLoader("user/get-users-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(usersResponse));
    }

    @Test
    @Order(2)
    @DisplayName("v1/users?name=Roger returns a list with an user")
    void findByName_ReturnsAListWithAnUserFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        String name = "Roger";

        var usersResponse = fileUtils.readResourceLoader("user/get-find-by-name-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("firstName", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(usersResponse));
    }

    @Test
    @Order(3)
    @DisplayName("v1/users?name=xaxa returns an empty list when name is not found")
    void findByName_ReturnsAnEmptyList_WhenNameIsNotFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        String name = "xaxa";

        String emptyListResponse = fileUtils.readResourceLoader("user/get-null-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("firstName", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(emptyListResponse));
    }

    @Test
    @Order(4)
    @DisplayName("v1/users/{id} returns an user when is found")
    void findByName_ReturnsAnUser_WhenIsFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        Long id = 1L;

        var userResponse = fileUtils.readResourceLoader("user/get-find-by-id-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userResponse));
    }

    @Test
    @Order(5)
    @DisplayName("v1/users/{id} throws a ResponseStatusHTTPException 404 when user not found")
    void findByName_ThrowsException404_WhenIsNotFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @Order(6)
    @DisplayName("v1/users/ creates an user")
    void saves_CreatesAnUser_WhenSuccessful() throws Exception {

        var userToSave = User.builder()
                .id(4L)
                .firstName("John")
                .lastName("Wick")
                .email("JohnWick@example.com")
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

        var userRequest = fileUtils.readResourceLoader("user/post-user-request-200.json");
        var userResponse = fileUtils.readResourceLoader("user/post-user-response-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(userResponse));
    }

    @Test
    @Order(7)
    @DisplayName("v1/users/{id} deletes an user")
    void delete_RemovesAnUser_WhenSuccessful() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(8)
    @DisplayName("v1/users/{id} throws a ResponseStatusHTPException 404")
    void delete_ThrowsAResponseStatusException404_WhenUserIsNotFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));

    }

    @Test
    @Order(9)
    @DisplayName("v1/users/ updates an user when successful")
    void update_UpdatesAnUser_WhenSuccessful() throws Exception {


        BDDMockito.when(userData.getUsers()).thenReturn(userList);


        var request = fileUtils.readResourceLoader("user/put-user-request-204.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("v1/users/ throws ResponseStatusException whenUserNotFound")
    void update_UpdatesThrowsAnException_WhenUserNotFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var request = fileUtils.readResourceLoader("user/put-user-request-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

}
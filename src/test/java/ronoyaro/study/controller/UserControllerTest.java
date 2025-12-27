package ronoyaro.study.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    @DisplayName("GET v1/users returns a list with all users")
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
    @DisplayName("GET v1/users?name=Roger returns a list with an user")
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
    @DisplayName("GET v1/users?name=xaxa returns an empty list when name is not found")
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
    @DisplayName("GET v1/users/{id} returns an user when is found")
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
    @DisplayName("GET v1/users/{id} throws a ResponseStatusHTTPException 404 when user not found")
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
    @DisplayName("POST v1/users creates an user")
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
    @DisplayName("DELETE v1/users/{id} deletes an user")
    void delete_RemovesAnUser_WhenSuccessful() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/users/{id} throws a ResponseStatusHTPException 404")
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
    @DisplayName("PUT v1/users updates an user when successful")
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
    @DisplayName("PUT v1/users throws ResponseStatusException whenUserNotFound")
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

    @ParameterizedTest
    @MethodSource("badRequestPostSource")
    @Order(11)
    @DisplayName("POST v1/users throws bad request when the field are null")
    void save_ReturnsBadRequest400_WhenFieldAreNull(String path, List<String> errors) throws Exception {


        var request = fileUtils.readResourceLoader("user/".concat(path));

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();

        Assertions.assertThat(exception).isNotNull();


        Assertions.assertThat(exception.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("badRequestPutSource")
    @Order(12)
    @DisplayName("PUT v1/users throws a bad request when the fields are empty or null")
    void update_ThrowsBadRequest400_WhenFieldsAreNull(String path, List<String> errors) throws Exception {

        var request = fileUtils.readResourceLoader("user/".concat(path));

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();

        Assertions.assertThat(exception).isNotNull();

        Assertions.assertThat(exception.getMessage()).contains(errors);

    }

    private static Stream<Arguments> badRequestPutSource() {

        var firstNameMessage = "The field 'firstName' is required";
        var lastNameMessage = "The field 'lastName' is required";
        var emailMessage = "The field 'email' is required";
        var allErrors = List.of(firstNameMessage, lastNameMessage, emailMessage);
        var emailInvalidError = Collections.singletonList("The email is invalid");

        return Stream.of(
                Arguments.of("put-user-blank-request-400.json", allErrors),
                Arguments.of("put-user-empty-request-400.json", allErrors),
                Arguments.of("put-user-email-invalid-request-400.json", emailInvalidError)
        );
    }


    private static Stream<Arguments> badRequestPostSource() {
        var firstNameMessage = "The field 'firstName' is required";
        var lastNameMessage = "The field 'lastName' is required";
        var emailMessage = "The field 'email' is required";
        var allErros = List.of(firstNameMessage, lastNameMessage, emailMessage);
        var emailError = Collections.singletonList("Email is invalid");

        return Stream.of(
                Arguments.of("post-user-null-fields-request-400.json", allErros),
                Arguments.of("post-user-blank-fields-request-400.json", allErros),
                Arguments.of("post-user-email-invalid-request-400.json", emailError)
        );
    }

}
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ronoyaro.study.domain.User;
import ronoyaro.study.mapper.UserMapperImpl;
import ronoyaro.study.repository.UserRepository;
import ronoyaro.study.service.UserService;
import ronoyaro.study.utils.FileUtils;
import ronoyaro.study.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({UserRepository.class, FileUtils.class, UserUtils.class, UserMapperImpl.class, UserService.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository repository;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = userUtils.getUsersList();
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/users returns a list with all users")
    void findAll_ReturnsAnUserWithAllUsers_WhenSuccessful() throws Exception {

        BDDMockito.when(repository.findAll()).thenReturn(userList);

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
        var usersResponse = fileUtils.readResourceLoader("user/get-find-by-name-response-200.json");

        String name = "Roger";

        var usersExpected = userList.stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(name))
                .toList();

        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(usersExpected);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("firstName", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(usersResponse));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/users?firstName=xaxa returns an empty list when name is not found")
    void findByName_ReturnsAnEmptyList_WhenNameIsNotFound() throws Exception {
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
        var userResponse = fileUtils.readResourceLoader("user/get-find-by-id-response-200.json");

        Long id = 1L;
        var userExpected = userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userResponse));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/users/{id} throws a ResponseStatusHTTPException 404 when user not found")
    void findByName_ThrowsException404_WhenIsNotFound() throws Exception {

        var response = fileUtils.readResourceLoader("user/get-user-not-found-response-404.json");

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
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

        Long id = 2L;

        var userExpected = userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userExpected);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/users/{id} throws a NotFoundException when user is not found")
    void delete_ThrowsANotFoundException_WhenUserIsNotFound() throws Exception {

        var response = fileUtils.readResourceLoader("user/delete-user-not-found-response-404.json");

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/users updates an user when successful")
    void update_UpdatesAnUser_WhenSuccessful() throws Exception {

        var request = fileUtils.readResourceLoader("user/put-user-request-204.json");

        Long id = 1L;

        var userExpected = userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userExpected);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("PUT v1/users throws NotFoundException when user is not found")
    void update_UpdatesThrowsANotFoundException_WhenUserNotFound() throws Exception {

        var request = fileUtils.readResourceLoader("user/put-user-request-404.json");
        var response = fileUtils.readResourceLoader("user/put-user-not-found-response-400.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
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
        var emailError = Collections.singletonList("The email is invalid");

        return Stream.of(
                Arguments.of("post-user-null-fields-request-400.json", allErros),
                Arguments.of("post-user-blank-fields-request-400.json", allErros),
                Arguments.of("post-user-email-invalid-request-400.json", emailError)
        );
    }

}
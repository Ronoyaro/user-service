package ronoyaro.study.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ronoyaro.study.domain.User;
import ronoyaro.study.mock.MockUser;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    @InjectMocks
    private UserRepository userRepository;
    @Mock
    private UserData userData;
    @InjectMocks
    private MockUser mockUser;

    private List<User> usersList = new ArrayList<>();

    @BeforeEach
    void init() {
        usersList = mockUser.getUsersList();
    }

    @Test
    @Order(1)
    @DisplayName("list all returns a list with all")
    void findAll_ReturnsAList_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var usersExpected = userRepository.findAll();

        Assertions.assertThat(usersExpected)
                .isNotNull()
                .hasSameElementsAs(usersList)
                .hasSize(usersList.size());

    }

    @Test
    @Order(2)
    @DisplayName("find by name returns a list with an user found")
    void findByName_ReturnsAListUserFound_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        String nameExpected = usersList.getFirst().getFirstName();

        var usersFoundExpected = userRepository.findByName(nameExpected);

        Assertions.assertThat(usersFoundExpected)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("find by name returns an empty list when the name argument is null")
    void findByName_ReturnsAnEmptyList_WhenNameArgumentIsNull() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var emptyListExpected = userRepository.findByName(null);
        Assertions.assertThat(emptyListExpected)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @Order(4)
    @DisplayName("find by id returns an optional user when the given id exists")
    void findById_ReturnsAnUser_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userExpected = usersList.getFirst();

        var userFound = userRepository.findById(userExpected.getId());

        Assertions
                .assertThat(userFound)
                .isPresent()
                .contains(userExpected);

    }

    @Test
    @Order(5)
    @DisplayName("find by id returns empty when the given id doesnt exists")
    void findById_ReturnsEmpty_WhenUserNotFound() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        Long id = 99L;

        var userExpected = userRepository.findById(id);

        Assertions.assertThat(userExpected)
                .isNotPresent();

    }

    @Test
    @Order(6)
    @DisplayName("save creates an user in the list")
    void save_CreatesAnUser_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        User userToSave = User.builder()
                .id(4L)
                .firstName("Ronald")
                .lastName("Ferreira")
                .email("ronaldferreira@example.com")
                .build();

        var userSaved = userRepository.save(userToSave);

        Assertions.assertThat(userToSave)
                .isEqualTo(userSaved)
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @Order(7)
    @DisplayName("deletes an user in the list")
    void delete_RemovesAnUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userExpected = usersList.getFirst();

        userRepository.delete(userExpected);

        Assertions.assertThat(usersList)
                .doesNotContain(userExpected);
    }

    @Test
    @Order(8)
    @DisplayName("update updates n user in the list")
    void update_UpdatesAnUser_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userFound = usersList.getFirst();

        User userToUpdate = User.builder()
                .id(userFound.getId())
                .firstName(userFound.getFirstName())
                .lastName(userFound.getLastName())
                .email("newemail@example.com")
                .build();

        userRepository.update(userToUpdate);

        Assertions.assertThat(usersList)
                .contains(userToUpdate);


    }
}
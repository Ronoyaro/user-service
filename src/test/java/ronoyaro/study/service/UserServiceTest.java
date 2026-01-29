package ronoyaro.study.service;

import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ronoyaro.study.domain.User;
import ronoyaro.study.exception.EmailAlreadyExistsException;
import ronoyaro.study.repository.UserRepository;
import ronoyaro.study.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserUtils userUtils;

    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {

        userList = userUtils.getUsersList();

    }

    @Test
    @Order(1)
    @DisplayName("find all returns a list with all animes when name argument is null")
    void findAll_ReturnsAListWithAllUsers_WhenNameArgumentIsNull() {

        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var usersExpected = service.findAll(null);

        Assertions.assertThat(usersExpected).containsAll(userList);

    }

    @Test
    @Order(2)
    @DisplayName("find all returns a list with a user found when name argument exists")
    void findAll_ReturnsAListWithAUserFound_WhenNameExists() {

        var userExpected = userList.getFirst();
        var singletonList = singletonList(userExpected);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(userExpected.getFirstName()))
                .thenReturn(singletonList);

        var users = service.findAll(userExpected.getFirstName());

        Assertions.assertThat(users)
                .isNotEmpty()
                .isSameAs(singletonList);
    }

    @Test
    @Order(3)
    @DisplayName("find all returns an empty list when name argument doesn't exists in the list")
    void findAll_ReturnsAnEmptyList_WhenNameArgumentDoesntExists() {

        var xaxa = "xaxa";
        List<User> emptyList = Collections.emptyList();

        BDDMockito.when(repository.findByFirstNameIgnoreCase(xaxa)).thenReturn(emptyList);

        var emptyListExpected = service.findAll(xaxa);

        Assertions.assertThat(emptyListExpected)
                .isEmpty();

    }

    @Test
    @Order(4)
    @DisplayName("find by id returns an user when his is found")
    void findById_ReturnsAUser_WhenUserIsFound() {

        var user = userList.getFirst();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertThatNoException().isThrownBy(() -> service.findByIdOrThrowNotFound(user.getId()));
    }

    @Test
    @Order(5)
    @DisplayName("find by id throws a ResponseStatusException 404 when user is not found")
    void findById_ThrowsAnExceptionWhenUserIsNotFound() {

        var userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findByIdOrThrowNotFound(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(6)
    @DisplayName("save creates an new user")
    void save_CreatesAnUser_WhenSuccessful() {

        var userToSave = userUtils.newUser();
        var userExpectedSaved = userUtils.newUserSaved();

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToSave)).thenReturn(userExpectedSaved);

        User userSaved = service.save(userToSave);

        Assertions.assertThat(userSaved).isEqualTo(userExpectedSaved);

    }

    @Test
    @Order(7)
    @DisplayName("delete removes an user when user is found")
    void delete_Removes_AnUser_WhenIsFound() {

        var userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(userToDelete.getId()));

    }

    @Test
    @Order(8)
    @DisplayName("delete throws a ResponseStatusException when User is not found")
    void delete_Removes_AnUser_WhenUserIsNotFound() {

        var userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.deleteById(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(9)
    @DisplayName("updates update an user when is found")
    void update_UpdatesAnUser_WhenIsFound() {
        User userToUpdate = userList.getFirst().withEmail("nanana@example.com");

        String email = userToUpdate.getEmail();
        Long id = userToUpdate.getId();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);


        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("update throws an NotFoundException when user is not found")
    void update_ThrowsAnException_WhenUserIsNotFound() {

        var userToUpdate = userList.getFirst().withEmail("nanana@example.com");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(11)
    @DisplayName("update throws an EmailAlreadyExistsException when email belongs to another user")
    void update_ThrowsEmailException_WhenAlreadyBelongsToAnotherUser() {
        var userSaved = userList.getLast();
        var userToUpdate = userList.getFirst().withEmail(userSaved.getEmail());

        String email = userToUpdate.getEmail();
        Long id = userToUpdate.getId();


        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(userSaved));

        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);

    }

    @Test
    @Order(12)
    @DisplayName("save throws an EmailAlreadyExists when Email belongs to another user")
    void save_ThrowsEmailException_WhenEmailBelongsToAnotherUser() {
        var userSaved = userList.getLast();
        var userToSave = userUtils.newUser().withEmail(userSaved.getEmail());

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.of(userSaved));

        Assertions.assertThatException().isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }


    private static @NonNull List<User> singletonList(User userExpected) {
        return Collections.singletonList(userExpected);
    }


}
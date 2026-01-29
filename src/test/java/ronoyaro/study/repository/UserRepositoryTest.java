package ronoyaro.study.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ronoyaro.study.config.TestcontainerBasicConfig;
import ronoyaro.study.domain.User;
import ronoyaro.study.utils.UserUtils;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({UserUtils.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Não está sendo usado, mas deixa ai.
//@Transactional(propagation = Propagation.NOT_SUPPORTED) Indica que não será feito um rollback no db
class UserRepositoryTest extends TestcontainerBasicConfig { //Utilizar testes de container no repositorio serve para testar o nosso banco

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUtils userUtils;

    @Test
    @Order(1)
    @DisplayName("save creates a new user")
    void save_CreatesANewUser_WhenSuccessful() {

        var userToSave = userUtils.newUser();
        User userSaved = repository.save(userToSave);

        Assertions.assertThat(userSaved)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(2)
    @DisplayName("find All returns a list with users")
    @Sql("/sql/init_one_user.sql")
    void findAll_ReturnsAllUsers() {
        var usersExpected = repository.findAll();

        Assertions.assertThat(usersExpected)
                .isNotEmpty();

    }
}
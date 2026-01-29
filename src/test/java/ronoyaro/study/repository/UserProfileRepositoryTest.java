package ronoyaro.study.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserProfileRepositoryTest {
    @Autowired
    private UserProfileRepository repository;

    @Test
    @Order(1)
    @DisplayName("findAllUsersByProfileId find all Users by ProfileId")
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findAllUsersByProfileId_ReturnsAllUsersByProfileId() {
        Long profileId = 1L;
        var allUsersByProfileId = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(allUsersByProfileId)
                .hasSize(2)
                .doesNotContainNull();
    }
}
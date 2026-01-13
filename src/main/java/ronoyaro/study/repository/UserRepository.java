package ronoyaro.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ronoyaro.study.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstNameIgnoreCase(String firstName);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIdNot(String email, Long id);

}

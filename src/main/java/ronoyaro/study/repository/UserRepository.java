package ronoyaro.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ronoyaro.study.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstNameIgnoreCase(String name);
}

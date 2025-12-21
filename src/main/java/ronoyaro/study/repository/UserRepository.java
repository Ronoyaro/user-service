package ronoyaro.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ronoyaro.study.domain.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public List<User> findByName(String name) {
        return userData.getUsers()
                .stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(name))
                .toList();
    }

}

package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ronoyaro.study.domain.User;
import ronoyaro.study.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }
}

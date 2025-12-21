package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
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

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "User not found"));
    }

    public User save(User userToSave) {
        return repository.save(userToSave);
    }

    public void update(User user) {
        var userToUpdate = findByIdOrThrowNotFound(user.getId());
        repository.update(userToUpdate);
    }

    public void delete(User user) {
        var userToDelete = findByIdOrThrowNotFound(user.getId());
        repository.delete(userToDelete);
    }
}

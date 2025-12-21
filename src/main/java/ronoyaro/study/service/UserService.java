package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ronoyaro.study.domain.User;
import ronoyaro.study.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
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
        userToSave.setId(ThreadLocalRandom.current().nextLong(1, 120));
        return repository.save(userToSave);
    }

    public void update(User userToUpdate) {
        var userFound = findByIdOrThrowNotFound(userToUpdate.getId());
        userToUpdate.setFirstName(userFound.getFirstName());
        userToUpdate.setLastName(userFound.getLastName());
        repository.update(userToUpdate);
    }

    public void deleteById(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }
}


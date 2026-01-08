package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ronoyaro.study.domain.User;
import ronoyaro.study.exception.NotFoundException;
import ronoyaro.study.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User userToSave) {
        userToSave.setId(ThreadLocalRandom.current().nextLong(1, 120));
        return repository.save(userToSave);
    }

    public void update(User userToUpdate) {
        findByIdOrThrowNotFound(userToUpdate.getId());
        repository.update(userToUpdate);
    }

    public void deleteById(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }
}


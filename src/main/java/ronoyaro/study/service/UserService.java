package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ronoyaro.study.domain.User;
import ronoyaro.study.exception.EmailAlreadyExistsException;
import ronoyaro.study.exception.NotFoundException;
import ronoyaro.study.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User userToSave) {
        assertEmailDoesNotExists(userToSave.getEmail());
        return repository.save(userToSave);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        assertEmailDoesNotExists(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    public void deleteById(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    public void assertEmailDoesNotExists(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwsEmailExistsException);
    }

    public void assertEmailDoesNotExists(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent((this::throwsEmailExistsException));
    }

    private void throwsEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("Email %s already exists".formatted(user.getEmail()));
    }


}


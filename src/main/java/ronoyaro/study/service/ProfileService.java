package ronoyaro.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ronoyaro.study.domain.Profile;
import ronoyaro.study.exception.NotFoundException;
import ronoyaro.study.repository.ProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Profile findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }

}

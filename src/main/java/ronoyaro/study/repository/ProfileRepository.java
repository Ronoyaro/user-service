package ronoyaro.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ronoyaro.study.domain.Profile;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByName(String name);
}

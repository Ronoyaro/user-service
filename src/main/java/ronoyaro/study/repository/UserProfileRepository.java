package ronoyaro.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ronoyaro.study.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}

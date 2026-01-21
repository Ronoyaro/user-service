package ronoyaro.study.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ronoyaro.study.domain.UserProfile;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p") //JPQL
    List<UserProfile> retrieveAll();

    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();
}

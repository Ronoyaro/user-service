package ronoyaro.study.mock;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ronoyaro.study.domain.User;

import java.util.ArrayList;
import java.util.List;

@Component
@ToString
@Getter
public class MockUser {
    private List<User> usersList = new ArrayList<>();

    public MockUser() {
        var roger = User.builder()
                .id(1L)
                .firstName("Roger")
                .lastName("Campbell")
                .email("RogerCampbell@example.com")
                .build();

        var george = User.builder()
                .id(2L)
                .firstName("George")
                .lastName("Marshall")
                .email("GeorgeMarshall@example.com")
                .build();

        var david = User.builder()
                .id(3L)
                .firstName("David")
                .lastName("Bale")
                .email("DavidBale@example.com")
                .build();

        usersList.addAll(List.of(roger, george, david));
    }


}

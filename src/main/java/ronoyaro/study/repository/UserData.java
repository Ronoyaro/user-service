package ronoyaro.study.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ronoyaro.study.domain.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserData {
    private final List<User> users = new ArrayList<>();

    public UserData() {
        var tomCar = User.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Car")
                .email("TomCCarr@example.com")
                .build();

        var burtonWells = User.builder()
                .id(2L)
                .firstName("Burton")
                .lastName("Wells")
                .email("BurtonWells@example.com")
                .build();

        var deborahGebhard = User.builder()
                .id(3L)
                .firstName("Deborah")
                .lastName("Gebhard")
                .email("DeborahGebhard@example.com")
                .build();
        users.addAll(List.of(tomCar, burtonWells, deborahGebhard));
    }
}

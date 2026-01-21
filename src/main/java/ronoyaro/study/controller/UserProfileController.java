package ronoyaro.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ronoyaro.study.domain.UserProfile;
import ronoyaro.study.service.UserProfileService;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll() {

        var profiles = service.findAll();

        return ResponseEntity.ok(profiles);
    }
}

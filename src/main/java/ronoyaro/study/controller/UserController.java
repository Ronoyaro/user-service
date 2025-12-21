package ronoyaro.study.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ronoyaro.study.domain.User;
import ronoyaro.study.service.UserService;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> listAll(@RequestParam(required = false) String name) {
        List<User> users = userService.findAll(name);
        return ResponseEntity.ok(users);
    }
}

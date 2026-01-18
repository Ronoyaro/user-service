package ronoyaro.study.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ronoyaro.study.domain.User;
import ronoyaro.study.dtos.UserPostRequestDTO;
import ronoyaro.study.dtos.UserPutRequestDTO;
import ronoyaro.study.dtos.UserResponseDTO;
import ronoyaro.study.mapper.UserMapper;
import ronoyaro.study.service.UserService;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll(@RequestParam(required = false) String firstName) {

        log.debug("Request to list all users from name '{}'", firstName);

        List<User> users = userService.findAll(firstName);

        List<UserResponseDTO> usersListResponseDTO = userMapper.toUsersListResponseDTO(users);

        return ResponseEntity.ok(usersListResponseDTO);

    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {

        log.debug("Request find user by id '{}'", id);

        User userFound = userService.findByIdOrThrowNotFound(id);

        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userFound);

        return ResponseEntity.ok(userResponseDTO);

    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserPostRequestDTO userPostRequestDTO) {

        log.debug("Request to save user '{}'", userPostRequestDTO.getFirstName());

        var userToSave = userMapper.toUser(userPostRequestDTO);

        var userSaved = userService.save(userToSave);

        var userResponseDTO = userMapper.toUserResponseDTO(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        log.debug("Request to delete user '{}'", id);

        userService.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequestDTO userPutRequestDTO) {
        log.debug("Request do update user '{}'", userPutRequestDTO.getId());

        User userToUpdate = userMapper.toUser(userPutRequestDTO);

        log.debug("user? {}", userToUpdate);

        userService.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

}

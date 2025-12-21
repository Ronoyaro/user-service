package ronoyaro.study.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ronoyaro.study.DTOs.UserPutRequestDTO;
import ronoyaro.study.DTOs.UserRequestDTO;
import ronoyaro.study.DTOs.UserResponseDTO;
import ronoyaro.study.domain.User;
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
    public ResponseEntity<List<UserResponseDTO>> listAll(@RequestParam(required = false) String name) {

        log.debug("Request to list all users from name '{}'", name);

        List<User> users = userService.findAll(name);

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
    public ResponseEntity<UserResponseDTO> save(@RequestBody UserRequestDTO userRequestDTO) {

        log.debug("Request to save user '{}'", userRequestDTO.getFirstName());

        var userToSave = userMapper.toUser(userRequestDTO);

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
    public ResponseEntity<Void> update(@RequestBody UserPutRequestDTO userPutRequestDTO) {
        log.debug("Request do update user '{}'", userPutRequestDTO.getId());

        User userToUpdate = userMapper.toUser(userPutRequestDTO);

        userService.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

}

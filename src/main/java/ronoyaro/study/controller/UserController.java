package ronoyaro.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ronoyaro.study.domain.User;
import ronoyaro.study.dtos.UserPostRequestDTO;
import ronoyaro.study.dtos.UserPutRequestDTO;
import ronoyaro.study.dtos.UserResponseDTO;
import ronoyaro.study.exception.BadRequestError;
import ronoyaro.study.exception.ErrorMessageDefault;
import ronoyaro.study.mapper.UserMapper;
import ronoyaro.study.service.UserService;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "User related endpoints") //muda o h1 da api no swagger, e a descrição
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping()//para o swagger identificar o mediatype
    @Operation(summary = "Get all users", description = "Get all users available in the system",
            responses = {
                    @ApiResponse(description = "List all users",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
            })
    public ResponseEntity<List<UserResponseDTO>> listAll(@RequestParam(required = false) String firstName) {

        log.debug("Request to list all users from name '{}'", firstName);

        List<User> users = userService.findAll(firstName);

        List<UserResponseDTO> usersListResponseDTO = userMapper.toUsersListResponseDTO(users);

        return ResponseEntity.ok(usersListResponseDTO);

    }

    @GetMapping("{id}")
    @Operation(summary = "Find user by id",
            responses = {
                    @ApiResponse(description = "Find user by its id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDTO.class))),

                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessageDefault.class)))
            })
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {

        log.debug("Request find user by id '{}'", id);

        User userFound = userService.findByIdOrThrowNotFound(id);

        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userFound);

        return ResponseEntity.ok(userResponseDTO);

    }

    @PostMapping
    @Operation(summary = "Save a new user", description = "Creates and save a new user in the system",
            responses = {
                    @ApiResponse(description = "save a user",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserPostRequestDTO.class))
                    ),
                    @ApiResponse(description = "throws Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestError.class))
                    )
            }

    )
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

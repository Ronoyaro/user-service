package ronoyaro.study.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ronoyaro.study.dtos.ProfileGetResponseDTO;
import ronoyaro.study.dtos.ProfilePostRequestDTO;
import ronoyaro.study.mapper.ProfileMapper;
import ronoyaro.study.service.ProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profiles")
@Slf4j
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponseDTO>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to list profile '{}'", name);
        var profiles = service.findAll(name);
        var responseDTO = mapper.toProfilesGetResponse(profiles);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfileGetResponseDTO> findById(@PathVariable Long id) {
        log.debug("Request to find profile by id '{}'", id);
        var profile = service.findById(id);
        var responseDTO = mapper.toProfileGetResponse(profile);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ProfileGetResponseDTO> save(@RequestBody @Valid ProfilePostRequestDTO profilePostRequestDTO) {
        log.debug("Request to save Profile '{}'", profilePostRequestDTO.getName());
        var profileToSave = mapper.toProfile(profilePostRequestDTO);
        var profileSaved = service.save(profileToSave);
        var responseDTO = mapper.toProfileGetResponse(profileSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}

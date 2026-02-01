package ronoyaro.study.controller;

import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import ronoyaro.study.config.TestcontainerBasicConfig;
import ronoyaro.study.dtos.ProfileGetResponseDTO;
import ronoyaro.study.dtos.ProfilePostRequestDTO;
import ronoyaro.study.utils.FileUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Teste roda numa porta aleatória
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerIT extends TestcontainerBasicConfig {

    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @Order(1)
    @Sql(value = "/sql/profile/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/profile/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("findAll returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() {

        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponseDTO>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        //testRestTemplate recebe uma URL, um tipo de Metodo, se tem algo a passar no corpo, e o o tipo do nosso retorno

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();

        responseEntity
                .getBody()
                .forEach(profileGetResponseDTO -> assertThat(profileGetResponseDTO).hasNoNullFieldsOrProperties());

    }

    @Test
    @Order(2)
    @DisplayName("findAll returns an empty list when nothing is found")
    void findAll_ReturnsAnEmptyList_WhenNothingIsFound() {

        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponseDTO>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isEmpty();

    }

    @Test
    @Order(3)
    @DisplayName("save creates a new profile")
    @Sql(value = "/sql/profile/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_Creates_ANewProfile() {

        var requestEntity = fileUtils.readResourceLoader("/profile/save-profile-request.json");

        var request = buildHttpEntity(requestEntity);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, request, ProfilePostRequestDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).hasNoNullFieldsOrProperties();


    }

    @ParameterizedTest
    @MethodSource(value = "PostBadRequest")
    @Order(4)
    @DisplayName("save throws a Bad Request when the fields are null or empty")
    void save_ThrowsBadRequest_WhenFieldsAreEmptyOrNull(String profile, String expectedResponse) {

        var response = fileUtils.readResourceLoader(expectedResponse);
        var profileToSave = fileUtils.readResourceLoader(profile);

        var request = buildHttpEntity(profileToSave);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, request, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity).isNotNull();

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .whenIgnoringPaths("timestamp")
                .isEqualTo(response);
    }

    private static Stream<Arguments> PostBadRequest() {


        return Stream.of(
                Arguments.of("/profile/null-profile.json", "/profile/save-message-bad-request-400.json"),
                Arguments.of("/profile/blank-profile.json", "/profile/save-message-bad-request-400.json")
        );

    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        HttpHeaders httpHeaders = new HttpHeaders(); //cria um Header
        httpHeaders.setContentType(MediaType.APPLICATION_JSON); //Passa o tipo do Header que é um JSON
        return new HttpEntity<>(request, httpHeaders); //Retorna um HTTPEntity com a requisição e o cabeçalho
    }
}

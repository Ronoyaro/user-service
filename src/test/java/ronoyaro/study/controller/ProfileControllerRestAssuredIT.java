package ronoyaro.study.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import ronoyaro.study.config.TestcontainerBasicConfig;
import ronoyaro.study.utils.FileUtils;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Teste roda numa porta aleatória
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerRestAssuredIT extends TestcontainerBasicConfig {
    private static final String URL = "/v1/profiles";
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort //pega a randomPort
    private int port;

    @BeforeEach
    void setUrl() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @Sql(value = "/sql/profile/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/profile/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/profiles returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() {

        var response = fileUtils.readResourceLoader("/profile/rest-assured/get-rest-assured-find-all.json");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/profiles returns an empty list when nothing is found")
    void findAll_ReturnsAnEmptyList_WhenNothingIsFound() {

        var response = fileUtils.readResourceLoader("/profile/rest-assured/get-rest-assured-find-all-empty.json");

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();

    }

    @Test
    @Order(3)
    @DisplayName("POST /v1/profiles returns an empty list when nothing is found")
    void save_createsANewProfile_WhenSuccessful() {

        var request = fileUtils.readResourceLoader("/profile/rest-assured/post-request-rest-assured-save.json");
        var expectedResponse = fileUtils.readResourceLoader("/profile/rest-assured/post-response-rest-assured-save.json");

        var response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource(value = "PostBadRequest")
    @Order(4)
    @DisplayName("save throws a Bad Request when the fields are null or empty")
    void save_ThrowsBadRequest_WhenFieldsAreEmptyOrNull(String requestJson, String expectedResponseJson) {
        var request = fileUtils.readResourceLoader(requestJson);

        var expectedResponse = fileUtils.readResourceLoader(expectedResponseJson);

        String response = given().when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> PostBadRequest() {

        return Stream.of(
                Arguments.of("/profile/rest-assured/post-request-rest-assured-blank.json", "/profile/rest-assured/post-rest-assured-bad-request-message.json"),
                Arguments.of("/profile/rest-assured/post-request-rest-assured-empty.json", "/profile/rest-assured/post-rest-assured-bad-request-message.json")
        );

    }


}

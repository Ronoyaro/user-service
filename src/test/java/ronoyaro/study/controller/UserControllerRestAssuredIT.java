package ronoyaro.study.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
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
import ronoyaro.study.repository.UserRepository;
import ronoyaro.study.utils.FileUtils;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Teste roda numa porta aleatória
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerRestAssuredIT extends TestcontainerBasicConfig {
    private static final String URL = "/v1/users";
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUrl() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/users returns a list with all profiles")
    void findAll_ReturnsAllUsers_WhenSuccessful() {

        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/get-rest-assured-find-all.json");

        var response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                    users.node("[2].id").asNumber().isPositive();
                });

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/users?firstName=Joseph returns an user queried by param")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByName_ReturnsAnUser_WhenSuccessful() {

        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/get-rest-assured-find-by-name.json");


        String response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("firstName", "Joseph")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);

    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/users returns an empty list when nothing is found")
    void findAll_ReturnsAnEmptyList_WhenNothingIsFound() {

        var response = fileUtils.readResourceLoader("/user/rest-assured/get-rest-assured-find-all-empty.json");

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
    @Order(4)
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /v1/users/{id} find user by id")
    void findById_ReturnsAnUser_ById_WhenSuccessful() {

        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/get-rest-assured-find-by-id.json");

        var users = repository.findByFirstNameIgnoreCase("Joseph");

        Assertions.assertThat(users)
                .hasSize(1);

        var response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", users.getFirst().getId())
                .when()
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);

    }

    @Test
    @Order(5)
    @DisplayName("GET v1/users/99 throws Not Found exception when User is not found")
    void findById_ReturnsException_WhenUser_IsNotFound() {
        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/not-found-user-messsage-rest-assured.json");

        Long id = 99L;

        var response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/users creates a new user")
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesANewUser_WhenSuccesful() {
        var request = fileUtils.readResourceLoader("/user/rest-assured/post-rest-assured-save.json");
        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/post-rest-assured-saved.json");

        String response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);

    }

    @Test
    @Order(7)
    @DisplayName("DELETE v1/users/{id} delete an user")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_RemoveAnUser_WhenSuccesful() {

        Long userId = repository.findAll().getFirst().getId();

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", userId)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/users/{id} throws a not found exception")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ThrowsNotFoundException_WhenUserIsNotFound() {

        var expectedResponse = fileUtils.readResourceLoader("/user/rest-assured/not-found-user-messsage-rest-assured.json");

        Long userId = 99L;

        String response = given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", userId)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);

    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/users/{id} updates an user")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updates_UpdateAnUser_WhenSuccesful() {

        var request = fileUtils.readResourceLoader("/user/rest-assured/put-rest-assured-update.json");
        var users = repository.findByFirstNameIgnoreCase("Joseph");

        Assertions.assertThat(users).hasSize(1);

        request = request.replace("1", users.getFirst().getId().toString());

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @Order(10)
    @DisplayName("PUT v1/users/{id} throws Not Found Exception when User is not found")
    void updates_ThrowsNotFoundException_WhenUserIsNotFound() {

        var request = fileUtils.readResourceLoader("/user/rest-assured/put-rest-assured-update.json");
        String response = fileUtils.readResourceLoader("user/rest-assured/not-found-user-messsage-rest-assured.json");

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(response))
                .log().all();

    }

    @ParameterizedTest
    @MethodSource(value = "PostBadRequest")
    @Order(11)
    @DisplayName("POST v1/users throws a Bad Request when the fields are null or empty")
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

    @ParameterizedTest
    @MethodSource(value = "PutBadRequest")
    @Order(12)
    @DisplayName("PUT v1/users throws a Bad Request when the fields are null or empty")
    void put_ThrowsBadRequest_WhenFieldsAreEmptyOrNull(String requestJson, String expectedResponseJson) {

        var request = fileUtils.readResourceLoader(requestJson);

        var expectedResponse = fileUtils.readResourceLoader(expectedResponseJson);

        String response = given().when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
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
                Arguments.of("/user/rest-assured/post-empty-rest-assured-bad-request.json", "/user/rest-assured/bad-request-message-rest-assured.json"),
                Arguments.of("/user/rest-assured/post-blank-rest-assured-bad-request.json", "/user/rest-assured/bad-request-message-rest-assured.json")
        );

    }
    private static Stream<Arguments> PutBadRequest() {

        return Stream.of(
                Arguments.of("/user/rest-assured/put-empty-rest-assured-bad-request.json", "/user/rest-assured/bad-request-message-rest-assured.json"),
                Arguments.of("/user/rest-assured/put-blank-rest-assured-bad-request.json", "/user/rest-assured/bad-request-message-rest-assured.json")
        );

    }


}

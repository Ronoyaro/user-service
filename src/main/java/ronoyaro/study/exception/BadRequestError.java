package ronoyaro.study.exception;

public record BadRequestError(String timestamp, Integer statusCode, String error, String message, String path) {
}

package ronoyaro.study.exception;

import java.time.Instant;

public record BadRequestError(Instant timestamp, Integer statusCode, String error, String message) {
}

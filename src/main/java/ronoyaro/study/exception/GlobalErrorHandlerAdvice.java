package ronoyaro.study.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDefault> handleNotFoundException(NotFoundException e) {
        var errorMessage = new ErrorMessageDefault(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDefault> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        var errorMessage = new ErrorMessageDefault(HttpStatus.BAD_REQUEST.value(), "duplicate entry for one of the unique fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        String errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.joining(", "));

        var badRequestError = new BadRequestError(OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyy'T'hh:mm:ss")),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestError);
    }

}

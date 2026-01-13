package ronoyaro.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

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

}

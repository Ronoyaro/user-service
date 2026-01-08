package ronoyaro.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDefault> handleNotFoundException(NotFoundException e) {
        var errorMessage = new ErrorMessageDefault(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }
}

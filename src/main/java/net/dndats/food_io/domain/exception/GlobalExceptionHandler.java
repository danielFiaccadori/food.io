package net.dndats.food_io.domain.exception;

import lombok.extern.slf4j.Slf4j;
import net.dndats.food_io.application.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleCustomerNotFound(CustomerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<BaseResponse<Void>> handleEmailConflict(EmailAlreadyRegisteredException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGenericException(Exception exception) {
        log.error("An unexpected error occurred: {}", exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("An unexpected error occurred: " + exception.getMessage()));
    }

}

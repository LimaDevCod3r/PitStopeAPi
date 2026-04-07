package dev.LimaDevCod3r.PitStopAPI.exception;

import dev.LimaDevCod3r.PitStopAPI.exception.response.ErrorResponse;
import dev.LimaDevCod3r.PitStopAPI.exception.response.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<ErrorResponse> handleInactive(InactiveResourceException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(new ErrorResponse(
                        "Resource Inactive",
                        HttpStatus.GONE.value(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        List.of(new FieldErrorResponse(null, ex.getMessage()))
                ));
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        new ErrorResponse(
                                "conflict",
                                HttpStatus.CONFLICT.value(),
                                request.getRequestURI(),
                                LocalDateTime.now(),
                                null
                        )
                );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "Not Found",
                        HttpStatus.NOT_FOUND.value(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        List.of(new FieldErrorResponse("id", ex.getMessage()))
                ));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "Conflict",
                        HttpStatus.CONFLICT.value(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        List.of(new FieldErrorResponse(null, ex.getMessage()))
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldErrorResponse)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Validation Error",
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        fieldErrors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Internal Server Error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        List.of(new FieldErrorResponse(null, ex.getMessage()))
                ));
    }

    private FieldErrorResponse toFieldErrorResponse(FieldError error) {
        return new FieldErrorResponse(
                error.getField(),
                error.getDefaultMessage()
        );
    }
}

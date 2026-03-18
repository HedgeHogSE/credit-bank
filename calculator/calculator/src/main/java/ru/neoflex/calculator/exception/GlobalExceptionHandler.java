package ru.neoflex.calculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<ErrorResponse> handleScoringException(ScoringException ex) {

        log.error("Scoring exception: {}", ex.getMessage(), ex);

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        List<ErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    log.error("Validation exception: {}", error.getDefaultMessage(), ex);
                    return new ErrorResponse(
                        error.getDefaultMessage()
                    );
                }
                )
                .toList();

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}

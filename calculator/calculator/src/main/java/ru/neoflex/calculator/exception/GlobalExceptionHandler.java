package ru.neoflex.calculator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        //log.error("Validation exception: {}", ex.getMessage(), ex);

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}

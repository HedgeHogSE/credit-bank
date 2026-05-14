package ru.neoflex.statement.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleRestClientException(RestClientResponseException ex) {

        log.error("External microservice call failed: status={}", ex.getStatusCode());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Error from external microservice",
                        "status", ex.getStatusCode().value(),
                        "message", ex.getResponseBodyAsString()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    log.error("Validation exception: {}", error.getDefaultMessage(), ex);
                    return error.getDefaultMessage();
                })
                .toList();

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Validation failed",
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "messages", errors
                ));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternalServiceException(
            ExternalServiceException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Service Unavailable",
                        "status", 503,
                        "message", ex.getMessage()
                ));
    }
}

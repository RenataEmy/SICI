package br.ifrn.edu.sici.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(TypeUserDenied.class)
    public ResponseEntity<Map<String, String>> forbidden(TypeUserDenied exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler({RuleConflit.class, DatabaseException.class})
    public ResponseEntity<Map<String, String>> conflict(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidBody(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst().orElse("Dados inválidos");
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> invalidParameter(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }
}

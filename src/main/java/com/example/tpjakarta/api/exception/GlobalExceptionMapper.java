package com.example.tpjakarta.api.exception;

import com.example.tpjakarta.api.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.OptimisticLockException;

@RestControllerAdvice
public class GlobalExceptionMapper {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(404, "Not Found", exception.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(409, "Business Conflict", exception.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorDTO> handleSecurityException(SecurityException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO(403, "Forbidden", exception.getMessage()));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorDTO> handleOptimisticLockException(OptimisticLockException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(409, "Optimistic Lock Exception", "The resource has been modified by another transaction."));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleAllExceptions(Throwable exception) {
        exception.printStackTrace(); // Log the error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(500, "Internal Server Error", exception.getMessage()));
    }
}

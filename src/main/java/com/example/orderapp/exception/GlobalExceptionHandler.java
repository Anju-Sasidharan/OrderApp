package com.example.orderapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleOrderNotFoundException(OrderNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex){
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField()+" : "+err.getDefaultMessage())
                .reduce((a,b)->a+" ; "+ b)
                .orElse("Validation Failed");
        return buildResponse(HttpStatus.BAD_REQUEST,message);
    }
    private ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status, String message){
        Map<String,Object> response = new HashMap<>();
        response.put("Timestamp", LocalDateTime.now());
        response.put("status",status.value());
        response.put("error",status.getReasonPhrase());
        response.put("message",message);
        return ResponseEntity.status(status).body(response);
    }
}

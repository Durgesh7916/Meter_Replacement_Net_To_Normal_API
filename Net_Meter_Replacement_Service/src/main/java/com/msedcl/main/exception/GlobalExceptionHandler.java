package com.msedcl.main.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String fieldName =
                            ((FieldError) error).getField();

                    String message =
                            error.getDefaultMessage();

                    errors.put(fieldName, message);
                });

        response.put("status", "FAILED");
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // CUSTOM NOT FOUND EXCEPTION
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", "FAILED");

        response.put("message",
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // General Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {

        String msg = ex.getMessage();

        Map<String, Object> res = new HashMap<>();

        System.out.println("Exception Type : "
                + ex.getClass().getName());

        System.out.println("Exception Message : "
                + msg);

        if (msg != null &&
                (msg.contains("url")
                || msg.contains("username")
                || msg.contains("password"))) {

            res.put("status", "FAILED");
            res.put("message",
                    "DATABASE CONFIGURATION MISSING ON SERVER");

            return ResponseEntity.status(500).body(res);
        }

        if (msg != null && msg.contains("ORA-01017")) {

            res.put("status", "FAILED");
            res.put("message",
                    "INVALID DATABASE CREDENTIALS");

            return ResponseEntity.status(500).body(res);
        }

        if (msg != null &&
                (msg.contains("Connection")
                || msg.contains("connect")
                || msg.contains("listener")
                || msg.contains("timeout"))) {

            res.put("status", "FAILED");
            res.put("message",
                    "DATABASE CONNECTION FAILED");

            return ResponseEntity.status(500).body(res);
        }

        // generic fallback
		/*
		 * res.put("status", "FAILED"); res.put("message", "INTERNAL SERVER ERROR");
		 */
        
        res.put("status", "FAILED");

        if (msg != null && !msg.isEmpty()) {

            res.put("message", msg);

        } else {

            res.put("message",
                    ex.getClass().getName());
        }

        return ResponseEntity.status(500).body(res);

       // return ResponseEntity.status(500).body(res);
    }
    
    
   
}
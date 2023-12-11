package com.mercan.weather.config;

import com.mercan.weather.exception.ApiError;
import com.mercan.weather.exception.QueryValidatorException;
import com.mercan.weather.exception.SensorNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Component
@RestControllerAdvice
public class WebExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @ExceptionHandler({SensorNotFound.class})
    public ResponseEntity<ApiError> handleNotFounds(Exception e) {;
        ApiError build = new ApiError(HttpStatus.NOT_FOUND.name() ,e.getMessage());
        return new ResponseEntity<>(build, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({QueryValidatorException.class})
    public ResponseEntity<ApiError> handleAPIValidation(Exception e) {;
        ApiError build = new ApiError(HttpStatus.BAD_REQUEST.name() ,e.getMessage());
        return new ResponseEntity<>(build, HttpStatus.BAD_REQUEST);
    }
}

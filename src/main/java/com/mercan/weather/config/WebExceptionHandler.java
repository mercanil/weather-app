package com.mercan.weather.config;

import com.mercan.weather.exception.ApiError;
import com.mercan.weather.exception.QueryValidatorException;
import com.mercan.weather.exception.SensorNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Component
@RestControllerAdvice
public class WebExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleException(MethodArgumentNotValidException exception) {

        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        ApiError build = new ApiError(HttpStatus.BAD_REQUEST.name() ,errorMsg);
        return new ResponseEntity<>(build, HttpStatus.BAD_REQUEST);
    }



}

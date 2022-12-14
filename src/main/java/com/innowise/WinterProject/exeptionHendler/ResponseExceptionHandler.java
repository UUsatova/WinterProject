package com.innowise.WinterProject.exeptionHendler;

import com.innowise.WinterProject.exeption.WrongIdException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    public ResponseEntity<Object> handleCityNotFoundException(
            WrongIdException ex, WebRequest request) {

        return ResponseEntity.badRequest().build();
    }
}

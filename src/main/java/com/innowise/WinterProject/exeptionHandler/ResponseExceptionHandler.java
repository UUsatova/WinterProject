package com.innowise.WinterProject.exeptionHandler;

import com.innowise.WinterProject.exeption.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<Object> handleWrongIdException(ItemNotFoundException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {


        List<String> strings = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toReadableFieldErrorString)
                .collect(Collectors.toList());
        return new ResponseEntity<>(strings, HttpStatus.BAD_REQUEST);
    }

    private String toReadableFieldErrorString(FieldError error) {
        return "Field error in object '" + error.getObjectName() + "' on field '" + error.getField() +
                "': rejected value [" + ObjectUtils.nullSafeToString(error.getRejectedValue()) + "]";
    }


}

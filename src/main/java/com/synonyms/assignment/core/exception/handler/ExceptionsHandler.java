package com.synonyms.assignment.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {
    @ExceptionHandler(AlreadyExistingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handler(AlreadyExistingException exception) {
        return exception.getError();
    }
}

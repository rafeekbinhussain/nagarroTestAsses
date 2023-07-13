package com.nagarro.exceptionhandler;

import com.nagarro.exception.InvalidRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;
import java.util.Map;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {InvalidRequestParameterException.class, ParseException.class})
    public Map<String, String> handle() {
        return Map.of("Error", "Invalid parameter passed");
    }
}

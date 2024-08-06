package com.example.demo.exception;

import com.example.demo.dto.TextResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<TextResponse> handleLanguageNotFoundException(LanguageNotFoundException ex) {
        return new ResponseEntity<>(new TextResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TranslationResourceAccessException.class)
    public ResponseEntity<TextResponse> handleTranslationResourceAccessException(TranslationResourceAccessException ex) {
        return new ResponseEntity<>(new TextResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TextResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new TextResponse("Произошла ошибка обработки запроса"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

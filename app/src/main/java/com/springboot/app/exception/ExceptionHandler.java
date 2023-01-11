package com.springboot.app.exception;

import com.springboot.app.payload.ErrorDescription;
import com.springboot.app.payload.ErrorField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Set<ErrorField> errorFields = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach(objectError ->
            errorFields.add(ErrorField.builder()
                    .fieldName(((FieldError) objectError).getField())
                    .message(objectError.getDefaultMessage())
                    .build()));
        return new ResponseEntity<>(errorFields, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("cause", ex.getCause().toString());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDescription> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                            WebRequest webRequest){
        ErrorDescription errorDescription = new ErrorDescription(new Date(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDescription, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDescription> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDescription errorDescription = new ErrorDescription(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDescription, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.example.socialnetwork.config;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author RKomov
 */
@ControllerAdvice
@RequestMapping(value = "/**")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @RequestMapping(value = {"/app/"}, method = RequestMethod.GET)
    public String router() {
        return "forward:/app/index.html";
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ResponseEntity<Object> handleDuplicateKey(
            Exception ex, WebRequest request) {
        logger.debug("",ex);
        return new ResponseEntity<>(null,
                new HttpHeaders(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(
            Exception ex, WebRequest request) {
        logger.debug("",ex);
        return new ResponseEntity<>(Map.of("errors", List.of(ex.getMessage())),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleAuthException(
            Exception ex, WebRequest request) {
        logger.debug("",ex);
        return new ResponseEntity<>(Map.of("errors", List.of(ex.getMessage())),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        logger.debug("",ex);
        Map<String, Object> body = new LinkedHashMap<>();

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(v -> v.getField() + " " + v.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("",ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Throwable findRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}

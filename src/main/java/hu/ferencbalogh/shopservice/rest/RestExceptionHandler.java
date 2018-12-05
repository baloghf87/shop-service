package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.exception.OrderNotFoundException;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req) {
        ex.printStackTrace();

        List<String> errors = ex.getBindingResult().getModel().values().stream()
                .filter(value -> value instanceof BeanPropertyBindingResult)
                .flatMap(value -> ((BeanPropertyBindingResult) value).getFieldErrors().stream())
                .map(fieldError -> String.format("%s.%s: %s", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest req) {
        return new ResponseEntity(String.format("Invalid value of property '%s': %s ", ex.getName(), ex.getValue()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity internalExceptions(Exception ex, WebRequest req) {
        ex.printStackTrace();

        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity otherExceptions(Exception ex, WebRequest req) {
        ex.printStackTrace();

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.nisum.test.msuser.exceptions;

import com.google.common.base.Joiner;
import com.nisum.test.msuser.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DataNotFoundException.class})
    protected ResponseEntity<ApiResponse<String>> handleDataNotFoundException(HttpServletRequest request, DataNotFoundException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), ex.getDescription());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler({DataDuplicatedException.class})
    protected ResponseEntity<ApiResponse<String>> handleDataDuplicateException(HttpServletRequest request,
        DataDuplicatedException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), ex.getDescription());
        return new ResponseEntity<>(response, status);
    }
    @Override protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
        final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String erorresMapeadosString = Joiner.on(",").withKeyValueSeparator("=").join(errors);
        ApiResponse<Object> response = new ApiResponse<>(erorresMapeadosString);
        return new ResponseEntity<>(response,
            HttpStatus.BAD_REQUEST);
    }

}
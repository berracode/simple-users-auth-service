package com.nisum.test.msuser.exceptions;

import com.google.common.base.Joiner;
import com.nisum.test.msuser.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
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

import static com.nisum.test.msuser.constants.Constants.CONSTRAINTS_ERROR;
import static com.nisum.test.msuser.constants.Constants.DATA_INTEGRITY_ERROR;

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
        logger.error(ex.toString());
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

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ApiResponse<String>> handleConstraintViolationException(Exception ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(
                new ApiResponse(CONSTRAINTS_ERROR, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataConstraintViolationException.class)
    public final ResponseEntity<ApiResponse<String>> handleDataIntegrityViolation(HttpServletRequest request,
                                                                                  DataConstraintViolationException ex){
        logger.error(request.getRequestURL().toString(), ex);
        return new ResponseEntity<>(new ApiResponse<>(
                DATA_INTEGRITY_ERROR,
                ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


}
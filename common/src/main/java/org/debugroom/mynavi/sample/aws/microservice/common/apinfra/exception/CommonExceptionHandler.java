package org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException businessException){
        businessException.setStackTrace(new StackTraceElement[]{});
        return new ResponseEntity<>(
                BusinessExceptionResponse.builder()
                        .businessException(businessException).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            Exception exception){
        BindingResult bindingResult = null;
        if(exception instanceof MethodArgumentNotValidException){
            bindingResult = ((MethodArgumentNotValidException)exception).getBindingResult();
        }else if(exception instanceof BindException){
            bindingResult = ((BindException)exception).getBindingResult();
        }
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return new ResponseEntity<>(
                ValidationErrorResponse.builder().validationErrors(
                        ValidationErrorMapper.map(fieldErrors)).build(),
                HttpStatus.BAD_REQUEST);
    }

}

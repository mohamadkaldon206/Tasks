package com.example.subscription_based_content.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final String MESSAGE_ATTRIBUTE = "errorMessage";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception ex) {
        ProblemDetail errorDetail = null;

        if (ex instanceof MethodArgumentNotValidException) {
            for (FieldError map : ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()) {
                errorDetail = ProblemDetail
                        .forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getMessage());
                errorDetail.setProperty(MESSAGE_ATTRIBUTE, "Missing Parameter " + map.getField() + " " + map.getDefaultMessage());
            }
            log.error("Missing Parameter {}" , ex.getMessage(), ex);

        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(405), ex.getMessage());
            errorDetail.setProperty(MESSAGE_ATTRIBUTE, "Method Not Allowed");
            log.error("Method Not Allowed " + ex.getMessage(), ex);

        } else {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), ex.getMessage());
            errorDetail.setProperty(MESSAGE_ATTRIBUTE, "Internal Server Error");
            log.error("Internal Server Error {}" , ex.getMessage(), ex);
        }

        if (errorDetail != null)
            errorDetail.setProperty("Exception Time ", LocalDateTime.now().format(formatter));

        return errorDetail;
    }

}

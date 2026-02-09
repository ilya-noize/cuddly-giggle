package org.shummi.mvc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.exception.ErrorMessageDto;
import org.shummi.mvc.exception.MvpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionController {
    private static final Logger log = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        String message = "Request verification error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[!] Received the status {} Error: {}\nStack trace:\r\n{}",
                status, message, e);

        return new ErrorMessageDto(message, LocalDateTime.now(), status);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageDto handleException(NoSuchElementException e) {

        return errorMessageAndLogging(e, "No such element", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MvpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleApplicationExceptions(MvpException e) {

        return errorMessageAndLogging(e, "Application logic failed", HttpStatus.BAD_REQUEST);
    }

    private ErrorMessageDto errorMessageAndLogging(Throwable e, String message, HttpStatus status) {
        log.error("Received the status {} Error: {}\n{}", status, message, e);

        return new ErrorMessageDto(message, LocalDateTime.now(), status);
    }
}
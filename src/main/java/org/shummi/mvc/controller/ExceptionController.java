package org.shummi.mvc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.exception.MvpException;
import org.shummi.mvc.exception.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {
    private final Logger log = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        String message = "Request verification error";
        String stacktrace = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(","));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[!] Received the status {} Error: {}\nStack trace:\r\n{}",
                status, message, stacktrace);

        return new ErrorMessageDto(message, stacktrace, LocalDateTime.now(), status);
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
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTraceString = sw.toString().replace(", ", "\n");
        log.error("Received the status {} Error: {}\n{}",
                status,
                message,
                stackTraceString
        );

        return new ErrorMessageDto(message, stackTraceString, LocalDateTime.now(), status);
    }
}
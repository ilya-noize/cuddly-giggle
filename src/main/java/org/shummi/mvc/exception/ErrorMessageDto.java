package org.shummi.mvc.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorMessageDto(
        String message,
        String stacktrace,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss.SSS'Z'"
        )
        LocalDateTime timestamp,
        HttpStatus status
) {
}
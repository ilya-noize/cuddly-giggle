package org.shummi.mvc.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserRequestDto(
        @NotBlank
        String name,

        @Email
        String email,

        @NotNull
        @Positive
        Integer age
) {
}

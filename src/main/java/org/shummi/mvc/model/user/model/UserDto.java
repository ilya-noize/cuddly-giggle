package org.shummi.mvc.model.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.shummi.mvc.model.user.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        @NotBlank
        String name,

        @Email
        String email,

        @NotNull
        @Positive
        Integer age
) {
    public User toEntity() {
        return new User(null, name, email, age, null);
    }
}

package org.shummi.mvc.model.pet;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PetRequestDto(
        @NotBlank String name
) {
}

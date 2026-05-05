package ExperienceGroup.Ludora.features.developer.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeveloperDtoRequest(

        @NotNull(message = "El ID de usuario no puede estar vacío")
        Long userId,

        @NotEmpty(message = "La compañía no puede estar vacía")
        String company
){
}


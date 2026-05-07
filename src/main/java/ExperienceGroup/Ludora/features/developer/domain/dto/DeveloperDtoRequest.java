package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeveloperDtoRequest(

        @NotNull(message = "El usuario no puede estar vacío")
        UserDTOResponse userDTOResponse,

        @NotEmpty(message = "La compañía no puede estar vacía")
        String company
){
}


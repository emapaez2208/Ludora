package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeveloperUpdateRequest(

        @Schema(description = "El nombre del developer", example = "Nahuel", required = true)
        @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
        @NotEmpty
        String name,

        @Schema(description = "El apellido del developer", example = "Suarez", required = true)
        @Size(min = 3, max = 32, message = "El apellido tiene una longitud minima de 3 y maxima de 32 caracteres")
        @NotEmpty
        String lastName,

        @Schema(description = "La compañía del developer", example = "Rito Games", required = true)
        @NotBlank
        String company
) {
}

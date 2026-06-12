package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeveloperDtoRequest(

        @Schema(description = "El nombre del developer", example = "Nahuel", required = true)
        @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
        @NotEmpty
        String name,

        @Schema(description = "El apellido del developer", example = "Suarez", required = true)
        @Size(min = 3, max = 32, message = "El apellido tiene una longitud minima de 3 y maxima de 32 caracteres")
        @NotEmpty
        String lastName,

        @Schema(description = "El nombre de usuario", example = "Fercho", required = true)
        @Size(min = 5, max = 20, message = "El usuario tiene una longitud minima de 5 y maxima de 20 caracteres")
        @NotBlank
        String userName,

        @Schema(description = "El email del developer", example = "Pepito@email.com", required = true)
        @NotNull
        Email email,

        @Schema(description = "Contraseña del developer")
        @NotNull
        Password password,

        @Schema(description = "La compañía del developer", example = "Rito Games", required = true)
        @NotBlank
        String company
) {
}
package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeveloperDtoRequest(

        @Schema(description = "The developer's name", example = "Nahuel", required = true)
        @Size(min = 3, max = 32, message = "The name must be between 3 and 32 characters long")
        @NotBlank
        String name,

        @Schema(description = "The developer's last name", example = "Suarez", required = true)
        @Size(min = 3, max = 32, message = "The last name must be between 3 and 32 characters long")
        @NotBlank
        String lastName,

        @Schema(description = "The username", example = "Fercho", required = true)
        @Size(min = 5, max = 20, message = "The username must be between 5 and 20 characters long")
        @NotBlank
        String userName,

        @Schema(description = "The developer's email", example = "Pepito@email.com", required = true)
        @NotNull
        Email email,

        @Schema(description = "The developer's password")
        @NotNull
        Password password,

        @Schema(description = "The developer's company", example = "Rito Games", required = true)
        @NotBlank
        String company
) {
}
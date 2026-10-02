package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeveloperUpdateRequest(

        @Schema(description = "The developer's name", example = "Nahuel", required = true)
        @Size(min = 3, max = 32, message = "The name must be between 3 and 32 characters long")
        @NotBlank
        String name,

        @Schema(description = "The developer's lastname", example = "Suarez", required = true)
        @Size(min = 3, max = 32, message = "The last name must be between 3 and 32 characters long")
        @NotBlank
        String lastName,

        @Schema(description = "The developer's company", example = "Rito Games", required = true)
        @NotBlank
        String company
) {
}

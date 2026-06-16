package ExperienceGroup.Ludora.features.ageRange.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AgeRangeDTORequest(
        @Schema(description = "Age category", example = "Suitable for all", required = true)
        @NotBlank(message = "The age category name cannot be empty")
        @Size(max = 50, message = "The name cannot exceed 50 characters")
        String rangeName,

        @Schema(description = "Minimum age required", example = "11", required = true)
        @NotNull(message = "The minimum age cannot be null")
        @PositiveOrZero(message = "The minimum age cannot be a negative number")
        @Max(value = 21, message = "The minimum age allowed cannot be greater than 21 years")
        Integer minAge,

        @Schema(description = "Description of the age category", example = "Contains violent content")
        @Size(max = 100, message = "The description cannot exceed 100 characters")
        String description
) {
}

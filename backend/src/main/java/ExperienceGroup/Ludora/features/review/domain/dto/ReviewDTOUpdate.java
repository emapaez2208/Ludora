package ExperienceGroup.Ludora.features.review.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewDTOUpdate(
        @Schema(description = "Game rating from 1 to 5", example = "4", required = true)
        @Min(value = 1, message = "The minimum rating is 1")
        @Max(value = 5, message = "The maximum rating is 5")
        @NotNull(message = "The rating is mandatory")
        Integer rating,

        @Schema(description = "Updated comment about the game", example = "After a few more hours, I changed my mind")
        @Size(max = 255, message = "The comment cannot exceed 255 characters")
        String comment
) {
}
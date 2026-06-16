package ExperienceGroup.Ludora.features.review.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record ReviewDTORequest(
                        @Schema(description = "Game rating from 1 to 5", example = "5", required = true )
                        @Min(value = 1, message = "The minimum rating is 1")
                        @Max(value = 5, message = "The maximum rating is 5")
                        @NotNull (message = "The rating is mandatory")
                        Integer rating,

                        @Schema(description = "Comment or opinion about the game", example = "This game is a little gem")
                        @Size(max = 255, message = "The comment cannot exceed 255 characters")
                        String comment,

                        @Schema(description = "External ID of the game to which the review belongs", required = true)
                        @NotNull UUID gameExternalId,

                        @Schema(description = "External ID of the user to whom the review belongs", required = true)
                        @NotNull UUID clientExternalId

){
}

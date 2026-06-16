package ExperienceGroup.Ludora.features.game.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GameDTORequest(
    @Schema(description = "Game name", example = "The Sims", required = true)
    @NotBlank(message = "The name cannot be empty")
    @Size (max = 50, message = "The name cannot exceed 50 characters")
    String name,

    @Schema(description = "Game price", example = "110.70", required = true)
    @NotNull(message = "The price cannot be null")
    @PositiveOrZero(message = "The price cannot be negative")
    BigDecimal price,

    @Schema(description = "Game description", example = "Life simulation game where you design homes, manage relationships, and fulfill aspirations.")
    @Size(max = 200, message = "The description cannot exceed 200 characters")
    String description,

    @Schema(description = "Game release date", example = "2017-03-17", required = true)
    @NotNull(message = "The release date cannot be null")
    @PastOrPresent(message = "The date cannot be in the future")
    LocalDate releaseDate,

    @Schema(description = "External age range ID", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull(message = "An age range must be provided for the game")
    UUID ageRangeExternalId,

    @Schema(description = "List of the game's genre names", example = "[shooter, rpg]", required = true)
    @NotEmpty(message = "The game must have at least one genre")
    List<String> genreNames

    ) {
}

package ExperienceGroup.Ludora.features.genre.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GenreDTO (@Schema(description = "Genre name" , example = "shooter" , required = true)
                               @Size (min = 0 , max = 20, message = "The maximum length of the name must be 20 characters")
                               @NotBlank String name ,
                               @Schema(description = "Genre description",required = true)
                               @NotNull String description){

}

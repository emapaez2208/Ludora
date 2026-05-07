package ExperienceGroup.Ludora.features.genre.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GenreDTO (@Schema(description = "Nombre del genero" , example = "shooter" , required = true)
                               @Size (min = 0 , max = 20, message = "La longuitud maxima del nombre tiene que ser 20 caracteres")
                               @NotBlank String name ,
                               @Schema(description = "Descripcion del genero",required = true)
                               @NotNull String description){

}

package ExperienceGroup.Ludora.features.review.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record ReviewDTORequest(
                        @Schema(description = "Calificacion del juego del 1 al 5", example = "5", required = true )
                        @Min(value = 1, message = "La calificacion minima es 1")
                        @Max(value = 5, message = "La calificacion maxima es 5")
                        @NotNull int rating,

                        @Schema(description = "Comentario u opinion del juego", example = "Este juego es una joyita")
                        @Size(max = 255, message = "El comentario no puede superar los 255 caracteres")
                        @NotBlank String comment,

                        @Schema(description = "ID externo del juego al que le pertenece la reseña", required = true)
                        @NotNull UUID gameExternalId,

                        @Schema(description = "ID externo del usuario al que le pertenece la reseña", required = true)
                        @NotNull UUID userExternalId

){
}

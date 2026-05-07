package ExperienceGroup.Ludora.features.ageRange.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AgeRangeDTORequest(
        @Schema(description = "Categoría de edad", example = "Apto para todos", required = true)
        @NotBlank(message = "El nombre de la categoría de edad no puede estar vacío")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String rangeName,

        @Schema(description = "Mínimo de edad requerido", example = "11", required = true)
        @NotNull(message = "El mínimo de edad no puede ser nula")
        @PositiveOrZero(message = "El mínimo de edad no puede ser un número negativo")
        Integer minAge,

        @Schema(description = "Descripción de la categoría de edad", example = "Contiene contenido violento")
        @Size(max = 100, message = "La descripción no puede superar los 100 caracteres")
        String description
) {
}

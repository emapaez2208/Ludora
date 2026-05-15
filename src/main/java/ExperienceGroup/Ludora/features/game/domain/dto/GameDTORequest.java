package ExperienceGroup.Ludora.features.game.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GameDTORequest(
    @Schema(description = "Nombre del juego", example = "The Sims", required = true)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size (max = 50, message = "El nombre no puede superar los 50 caracteres")
    String name,

    @Schema(description = "Precio del juego", example = "110.70", required = true)
    @NotNull(message = "El precio no puede ser nulo")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    BigDecimal price,

    @Schema(description = "ID externo del desarrollador", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull(message = "Debe existir un desarrollador para el juego")
    UUID developerExternalId,

    @Schema(description = "Descripción del juego", example = "Juego de simulación de vida donde diseñas hogares, gestionas relaciones y cumples aspiraciones.")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    String description,

    @Schema(description = "Fecha de lanzamiento del juego", example = "2017-03-17", required = true)
    @NotNull(message = "La fecha de lanzamiento no puede ser nula")
    @PastOrPresent(message = "La fecha no puede ser futura")
    LocalDate releaseDate,

    @Schema(description = "ID externo del rango de edad", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull(message = "Debe existir un rango de edad para el juego")
    UUID ageRangeExternalId,

    @Schema(description = "Lista de los ID de los géneros del juego", example = "[1, 4]", required = true)
    @NotEmpty(message = "El juego debe tener al menos un género")
    List<Long> genreIds
    ) {
}

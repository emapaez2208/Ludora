package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaleDTORequest(
        @Schema(description = "ID of the client making the purchase", example = "1", required = true)
        @NotNull(message = "The client ID cannot be null")
        UUID clientExternalId
) {
}
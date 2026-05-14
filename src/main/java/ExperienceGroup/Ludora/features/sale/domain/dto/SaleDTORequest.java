package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SaleDTORequest(
        @Schema(description = "ID del cliente que realiza la compra", example = "1", required = true)
        @NotNull UUID clientExternalId,

        @Schema(description = "Lista de IDs de los juegos comprados", example = "[e4b3c8a1-8f2d-4c91-b3a5-7f8e9d0c1b2a,7d1a5b9c-2e3f-4a0b-8c9d-1e2f3a4b5c6d]", required = true)
        @NotEmpty List<UUID> gameExternalId,

        @Schema(description = "Estado inicial de la venta", example = "PENDING", required = true)
        @NotNull ESaleStatus status
) {
}
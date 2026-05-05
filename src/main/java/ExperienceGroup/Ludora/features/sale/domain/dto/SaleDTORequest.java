package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleDTORequest(
        @Schema(description = "ID del cliente que realiza la compra", example = "1", required = true)
        @NotNull ClientDTORequest client,

        @Schema(description = "Lista de IDs de los juegos comprados", example = "[10, 22, 5]", required = true)
        @NotEmpty List<GameDTORequest> games,

        @Schema(description = "Estado inicial de la venta", example = "PENDING", required = true)
        @NotNull ESaleStatus status
) {
}
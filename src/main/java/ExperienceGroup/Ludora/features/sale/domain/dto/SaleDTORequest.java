package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleDTORequest(
        @Schema(description = "ID del cliente que realiza la compra", example = "1", required = true)
        @NotNull ClientDTOResponse client,

        @Schema(description = "Lista de IDs de los juegos comprados", example = "[10, 22, 5]", required = true)
        @NotEmpty List<GameDTOResponse> games,

        @Schema(description = "Estado inicial de la venta", example = "PENDING", required = true)
        @NotNull ESaleStatus status
) {
}
package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaleDTORequest(
        @Schema(description = "ID del cliente que realiza la compra", example = "1", required = true)
        @NotNull UUID clientExternalId,

        @Schema(description = "Estado inicial de la venta", example = "PENDING", required = true)
        @NotNull ESaleStatus status
) {
}
package ExperienceGroup.Ludora.features.sale.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SaleDTORequest(
        @Schema(description = "ID del cliente que realiza la compra", example = "1", required = true)
        @NotNull Long clientId,

        @Schema(description = "Lista de IDs de los juegos comprados", example = "[10, 22, 5]", required = true)
        @NotEmpty List<Long> gameIds,

        @Schema(description = "Estado inicial de la venta", example = "Pendiente", required = true)
        @NotNull String status
) {
}
package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminDTO(@Schema(description = "El usuario base del administrador", required = true)
                              @NotNull
                              UserDTOResponse user,
                       @Schema(description = "El legajo del administrador", example = "611305", required = true)
                              @Positive
                              @NotNull
                              Long employeeId
                            ) {
}

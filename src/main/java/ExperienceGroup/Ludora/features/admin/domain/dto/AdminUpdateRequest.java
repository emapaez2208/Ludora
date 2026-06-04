package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AdminUpdateRequest (@Schema(description = "El nombre del usuario", example = "John", required = true)
                                  @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
                                  @NotEmpty
                                  String name,

                                  @Schema(description = "El apellido del usuario", example = "Doe", required = true)
                                  @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
                                  @NotEmpty
                                  String lastName,

                                  @Schema(description = "El legajo del administrador", example = "611305", required = true)
                                  @Positive
                                  @NotNull
                                  Long employeeId) {
}

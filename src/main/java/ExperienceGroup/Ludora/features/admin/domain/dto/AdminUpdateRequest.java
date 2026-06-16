package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AdminUpdateRequest (@Schema(description = "The user's name", example = "John", required = true)
                                  @Size(min = 3, max = 32, message = "The name must be between 3 and 32 characters long")
                                  @NotBlank
                                  String name,

                                  @Schema(description = "The user's last name", example = "Doe", required = true)
                                  @Size(min = 3, max = 32, message = "The last name must be between 3 and 32 characters long")
                                  @NotBlank
                                  String lastName,

                                  @Schema(description = "The administrator employee ID", example = "611305", required = true)
                                  @Positive
                                  @NotNull
                                  Long employeeId) {
}

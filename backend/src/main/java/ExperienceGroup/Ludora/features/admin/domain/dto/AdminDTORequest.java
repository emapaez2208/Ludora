package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AdminDTORequest(@Schema(description = "The user's name", example = "John", required = true)
                              @Size(min = 3, max = 32, message = "The name must be between 3 and 32 characters long")
                              @NotBlank
                              String name,

                              @Schema(description = "The user's last name", example = "Doe", required = true)
                              @Size(min = 3, max = 32, message = "The last name must be between 3 and 32 characters long")
                              @NotBlank
                              String lastName,

                              @Schema(description = "The username to register or log in", example = "JohnDoe", required = true)
                              @Size(min = 5, max = 20, message = "The username must be between 5 and 20 characters long")
                              @NotBlank
                              String userName,

                              @Schema(description = "The email must have a valid email format", example = "JohnDoe@email.com", required = true)
                              @NotNull
                              Email email,

                              @Schema(description = "The password to access the account, must have a valid format, \" +\n" +
                                      "                                      \"using at least one lowercase, one uppercase and one number." +
                                      "The length must be between 8 and 16 characters")
                              @NotNull
                              Password password,

                              @Schema(description = "The administrator employee ID", example = "611305", required = true)
                              @Positive
                              @NotNull
                              Long employeeId) {
}

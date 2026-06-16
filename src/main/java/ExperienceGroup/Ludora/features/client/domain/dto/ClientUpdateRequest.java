package ExperienceGroup.Ludora.features.client.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientUpdateRequest(@Schema(description = "The user's name", example = "John", required = true)
                                  @Size(min = 3, max = 32, message = "The name must be between 3 and 32 characters long")
                                  @NotBlank
                                  String name,

                                  @Schema(description = "The user's last name", example = "Doe", required = true)
                                  @Size(min = 3, max = 32, message = "The last name must be between 3 and 32 characters long")
                                  @NotBlank
                                  String lastName,

                                  @Schema(description = "Phone number",example = "123" , required = true)
                                  @Positive(message = "Cannot be a negative number")
                                  @NotNull
                                  @Min(value = 1000000L, message = "The phone number must have at least 7 digits")
                                  @Max(value = 99999999999L, message = "The phone number cannot exceed 11 digits")
                                  Long phone ,

                                  @Schema(description = "Street name", example = "Luro" , required = true)
                                  @Size(max = 25 , message = "Must have a maximum of 25 characters")
                                  @NotNull
                                  String street,

                                  @Schema (description = "Street number",example = "123",required = true)
                                  @Positive(message = "Cannot be a negative numbero")
                                  @NotNull
                                  @Digits(integer = 5, fraction = 0, message = "The maximum number of digits allowed is 5")
                                  Integer numberStreet,

                                  @Schema(description = "Date of birth, format = YYYY-MM-DD", example = "2010-05-27", required = true)
                                  @NotNull LocalDate birthDate
) {
}

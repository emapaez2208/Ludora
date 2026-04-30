package ExperienceGroup.Ludora.features.user.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDTORequest(@NotEmpty String name,
                             @NotEmpty String lastName,
                             @NotBlank String userName,
                             @NotNull Email email,
                             @NotNull Password password) {

}

package ExperienceGroup.Ludora.common.utils;

import jakarta.validation.constraints.NotBlank;


public record AdminChangePasswordDTO(@NotBlank(message = "newPass is required") String newPass) {
}
 
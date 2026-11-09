package ExperienceGroup.Ludora.common.utils;

import jakarta.validation.constraints.NotNull;


public record AdminChangeEmailDTO(@NotNull(message = "newEmail is required") Email newEmail) {
}
 
package ExperienceGroup.Ludora.common.exception.dto;

import ExperienceGroup.Ludora.common.utils.Password;

public record ChangePasswordDTO(Password oldPass, Password newPass) {
}

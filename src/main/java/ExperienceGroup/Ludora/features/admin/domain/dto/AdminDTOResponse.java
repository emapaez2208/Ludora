package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminDTOResponse(
                              UserDTOResponse user,
                              Long employeeId
                            ) {
}

package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AdminDTOResponse(UUID externalId,
                               String name,
                               String lastName,
                               String userName,
                               Long employeeId
                            ) {
}

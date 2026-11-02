package ExperienceGroup.Ludora.features.admin.domain.dto;

import java.util.UUID;

public record AdminDTOResponse(UUID externalId,
                               String name,
                               String lastName,
                               String email,
                               String userName,
                               Long employeeId
                            ) {
}

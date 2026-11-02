package ExperienceGroup.Ludora.features.developer.domain.dto;

import java.util.UUID;

public record DeveloperDtoResponse(UUID externalId,
                                   String name,
                                   String lastName,
                                   String email,
                                   String userName,
                                   String company,
                                   Boolean statusBlocked
) {

}

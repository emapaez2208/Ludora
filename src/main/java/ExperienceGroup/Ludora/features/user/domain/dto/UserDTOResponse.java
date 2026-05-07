package ExperienceGroup.Ludora.features.user.domain.dto;

import java.util.UUID;

public record UserDTOResponse(UUID externalId,
                              String name,
                              String lastName,
                              String userName) {

}

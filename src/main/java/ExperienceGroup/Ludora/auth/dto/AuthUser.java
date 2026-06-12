package ExperienceGroup.Ludora.auth.dto;

import java.util.UUID;

public record AuthUser(UUID externalId, String username) {
}

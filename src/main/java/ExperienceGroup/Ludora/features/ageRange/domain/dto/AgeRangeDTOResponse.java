package ExperienceGroup.Ludora.features.ageRange.domain.dto;

import java.util.UUID;

public record AgeRangeDTOResponse(
        UUID externalId,
        String rangeName,
        Integer minAge,
        String description
) {
}

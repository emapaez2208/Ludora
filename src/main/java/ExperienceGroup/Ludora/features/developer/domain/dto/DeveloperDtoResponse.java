package ExperienceGroup.Ludora.features.developer.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

public record DeveloperDtoResponse(
        UserDTOResponse user,
        String company
) {

}

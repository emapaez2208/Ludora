package ExperienceGroup.Ludora.features.review.domain.dto;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDTOResponse(UUID externalId,
                                int rating,
                                String comment,
                                LocalDateTime date,
                                String userName,
                                String gameTitle
                                ){


}

package ExperienceGroup.Ludora.features.review.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDTOResponse(UUID externalId,
                                int rating,
                                String comment,
                                LocalDateTime date,
                                String username,
                                String gameTitle
                                ){


}

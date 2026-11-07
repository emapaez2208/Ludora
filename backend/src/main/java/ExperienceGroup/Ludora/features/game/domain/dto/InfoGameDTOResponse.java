package ExperienceGroup.Ludora.features.game.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InfoGameDTOResponse(
        UUID externalId,
        String name,
        String frontPage,
        BigDecimal price,
        BigDecimal discountedPrice,
        String company
) {

}
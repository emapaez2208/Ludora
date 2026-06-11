package ExperienceGroup.Ludora.features.game.domain.dto;

import java.math.BigDecimal;

public record InfoGameDTOResponse(
        String name,
        BigDecimal price,
        String company
) {
}
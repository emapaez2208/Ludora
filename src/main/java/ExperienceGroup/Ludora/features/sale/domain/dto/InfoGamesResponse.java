package ExperienceGroup.Ludora.features.sale.domain.dto;

import java.math.BigDecimal;

public record InfoGamesResponse(
        String name,
        BigDecimal price,
        String company
) {
}
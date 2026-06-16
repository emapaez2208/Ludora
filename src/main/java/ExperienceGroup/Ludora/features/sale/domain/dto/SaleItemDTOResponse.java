package ExperienceGroup.Ludora.features.sale.domain.dto;

import java.math.BigDecimal;

public record SaleItemDTOResponse(
        String gameName,
        String company,
        BigDecimal pricePaid
) {
}

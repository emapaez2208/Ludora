package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.game.domain.dto.InfoGameDTOResponse;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SaleDTOResponse ( UUID externalId,
                                LocalDateTime date,
                                ESaleStatus status,
                                BigDecimal totalPrice,
                                Integer earnedPoints,
                                String userName,
                                List<InfoGameDTOResponse> games
){
}

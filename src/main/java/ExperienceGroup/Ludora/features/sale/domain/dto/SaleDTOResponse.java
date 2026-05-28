package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SaleDTOResponse ( UUID externalId,
                                LocalDateTime date,
                                ESaleStatus status,
                                BigDecimal totalPrice,
                                String userName,
                                List<InfoGamesResponse> games
){
}

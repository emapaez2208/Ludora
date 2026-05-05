package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDTOResponse (LocalDateTime date,
                                ESaleStatus status,
                                Double totalPrice,
                                ClientDTOResponse client,
                                List<GameDTOResponse> games
){
}

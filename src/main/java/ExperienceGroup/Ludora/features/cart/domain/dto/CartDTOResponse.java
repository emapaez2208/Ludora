package ExperienceGroup.Ludora.features.cart.domain.dto;


import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;

import java.util.List;

public record CartDTOResponse(
        ClientDTOResponse client,
        List<GameDTOResponse> games,
        Double totalPrice
) {}
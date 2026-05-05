package ExperienceGroup.Ludora.features.cart.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

public record CartDTOResponse(
        UserDTOResponse user,
        List<GameDTOResponse> games,
        Double totalPrice
) {}
package ExperienceGroup.Ludora.features.cart.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CartDTORequest(
        Long userId,
        Long gameId) {
}

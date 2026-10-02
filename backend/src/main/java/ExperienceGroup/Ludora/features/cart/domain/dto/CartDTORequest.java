package ExperienceGroup.Ludora.features.cart.domain.dto;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.InfoGameDTOResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartDTORequest(

        @NotNull(message = "The user cannot be empty")
        @Schema(description = "User who owns the cart")
        ClientDTOResponse clientDTORequest,


        @Schema(description = "List of games in the cart")
        List<InfoGameDTOResponse> gamesResponse
) {

}

package ExperienceGroup.Ludora.features.cart.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartDTORequest(

        @NotNull(message = "El usuario no puede estar vacío")
        @Schema(description = "Usuario dueño del carrito")
        ClientDTOResponse clientDTORequest,


        @Schema(description = "Lista de juegos en carrito")
        List<GameDTOResponse> gamesResponse
) {

}

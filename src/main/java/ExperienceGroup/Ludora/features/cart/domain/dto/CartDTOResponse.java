package ExperienceGroup.Ludora.features.cart.domain.dto;


public record CartDTOResponse(
        ClientDTOResponse client,
        List<GameDTOResponse> games,
        Double totalPrice
) {}
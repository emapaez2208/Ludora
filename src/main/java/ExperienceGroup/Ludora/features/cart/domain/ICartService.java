package ExperienceGroup.Ludora.features.cart.domain;

import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTORequest;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;



public interface ICartService {

    CartDTOResponse save(CartDTORequest cartDTORequest);

    void addGameToCart(Long Client, Long gameId);

    Double PrecioTotal();

    void removeGameFromCart(Long Client, Long gameId);

    void deleteByClientId(Long clientId);




}

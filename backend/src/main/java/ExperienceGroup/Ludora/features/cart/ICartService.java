package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;

import java.util.UUID;

public interface ICartService {

    CartDTOResponse getCartByClient(UUID clientExternalId);

    CartDTOResponse getMyCart();

    CartDTOResponse addGame(UUID clientExternalId, UUID gameExternalId); /// agrego 1 juego al carrito

    CartDTOResponse removeGame(UUID clientExternalId, UUID gameExternalId);  ///borro 1 juego del carrito

    void clearCart(UUID clientExternalId);      /// limpiar el carrito sin borrarlo

    CartDTOResponse createCart (UUID clientExternalId);

}

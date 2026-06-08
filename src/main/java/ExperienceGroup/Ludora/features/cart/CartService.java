package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import ExperienceGroup.Ludora.features.cart.domain.mapper.ICartResponseMapper;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final IGameRepository gameRepository;
    private final ICartResponseMapper cartResponseMapper;
    private final IClientRepository clientRepository;

///-----------------------------------------------------------------------------------------///
/// Logica
 ///--------------------------------------------------------------------------------------------------///
    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientExternalId == authentication.principal.externalId")
    public CartDTOResponse getCartByClient(UUID clientExternalId) {
        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado para el cliente: " + clientExternalId));
        ;
        return cartResponseMapper.toDTO(cart);
    }

    /// agrego juego al carrito

    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId and hasAuthority('GAME_AGREE_CART')")
    public CartDTOResponse addGame(UUID clientExternalId, UUID gameExternalId) {
        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("El cliente no existe: " + clientExternalId)) ;

        GameEntity game = gameRepository.findByExternalId(gameExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado con id: " + gameExternalId));

        if (cart.getGames().contains(game)) {
            throw new IllegalStateException("El juego ya está en el carrito");
        }

        cart.getGames().add(game);

        cart.setTotalPrice(
                        cart.getGames().stream()
                                        .mapToDouble(g -> g.getPrice().doubleValue())
                                        .sum()
        );

        return cartResponseMapper.toDTO(cartRepository.save(cart));
    }

    /// quito 1 juego del carrito

    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId or hasRole('ADMIN')")
    public CartDTOResponse removeGame(UUID clientExternalId, UUID gameExternalId) {
        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("El cliente no existe: " + clientExternalId));

        GameEntity game = gameRepository.findByExternalId(gameExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Juego no encontrado con id: " + gameExternalId));

        if (!cart.getGames().remove(game)) {
            throw new EntityNotFoundException("El juego no está en el carrito");
        }

        cart.setTotalPrice(cart.getGames().stream()
                .mapToDouble(g -> g.getPrice().doubleValue())
                .sum());

        return cartResponseMapper.toDTO(cartRepository.save(cart));
    }



    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId or hasRole('ADMIN')")
    public void clearCart(UUID clientExternalId) {   /// este sirve para despues de la ventaa

        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                                         .orElseThrow(() -> new EntityNotFoundException("El cliente no existe: " + clientExternalId)) ;


        cart.setGames(new ArrayList<>());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    ///  el cliente deberia crear un cliente en transacctional-- primero crear el cliente guardarlo y crear el carrito
    ///  envia el id aca, lo busca en la lista
    ///  se crea el nuevo carrito
    ///  se le setean los atributos
    ///  1% logica 99% FE

    public CartDTOResponse crearCarrito(UUID clientExternalId) {
        ClientEntity client = clientRepository.findByExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + clientExternalId));

        CartEntity cart = new CartEntity();
        cart.setClient(client);
        cart.setGames(new ArrayList<>());
        cart.setTotalPrice(0.0);

        return cartResponseMapper.toDTO(cartRepository.save(cart));
    }
}



///quitar logica reperita (madularizar para la gente bonita)

/// de buscar por external id  y la de la su matoria

 /*
    private CartEntity findCartByClientExternalId(UUID clientExternalId) {
        return cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado para el cliente: " + clientExternalId));
    }



 */
package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.auth.providers.AuthenticatedUserProvider;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import ExperienceGroup.Ludora.features.cart.domain.mapper.ICartResponseMapper;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.InfoGameDTOResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final IGameRepository gameRepository;
    private final ICartResponseMapper cartResponseMapper;
    private final IClientRepository clientRepository;
    private final AuthenticatedUserProvider authenticatedUser;

    private static final Integer POINTS_THRESHOLD = 10000;
    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(0.10);


    ///-----------------------------------------------------------------------------------------///
/// Logica
 ///--------------------------------------------------------------------------------------------------///


    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientExternalId == authentication.principal.externalId")
    public CartDTOResponse getCartByClient(UUID clientExternalId) {
        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado para el cliente: " + clientExternalId));
        return cartResponseMapper.toDTO(cart);
    }

    @Override
    public CartDTOResponse getMyCart(){
        return getCartByClient(authenticatedUser.getCurrentUser().externalId());
    }

    /// agrego juego al carrito

    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId and hasAuthority('GAME_AGREE_CART')")
    public CartDTOResponse addGame(UUID clientExternalId, UUID gameExternalId) {
        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + clientExternalId));

        GameEntity game = gameRepository.findByExternalId(gameExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameExternalId));

        if (cart.getGames().contains(game)) {
            throw new IllegalStateException("The game is already in the cart");
        }

        cart.getGames().add(game);

        return recalculateCart(cart);
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

        return recalculateCart(cart);
    }



    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId or hasRole('ADMIN')")
    public void clearCart(UUID clientExternalId) {   /// este sirve para despues de la ventaa

        CartEntity cart = cartRepository.findByClient_ExternalId(clientExternalId)
                                         .orElseThrow(() -> new EntityNotFoundException("El cliente no existe: " + clientExternalId)) ;


        cart.setGames(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    ///  el cliente deberia crear un cliente en transacctional-- primero crear el cliente guardarlo y crear el carrito
    ///  envia el id aca, lo busca en la lista
    ///  se crea el nuevo carrito
    ///  se le setean los atributos
    ///  1% logica 99% FE

    public CartDTOResponse createCart(UUID clientExternalId) {
        ClientEntity client = clientRepository.findByExternalId(clientExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + clientExternalId));

        CartEntity cart = new CartEntity();
        cart.setClient(client);
        cart.setGames(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);

        return cartResponseMapper.toDTO(cartRepository.save(cart));
    }


    private CartDTOResponse recalculateCart(CartEntity cart){
        ClientEntity client = cart.getClient();
        boolean qualifiesForDiscount = client.getPoints() >= POINTS_THRESHOLD;

        BigDecimal totalPrice = cart.getGames().stream()
                .map(g -> {
                    BigDecimal price = g.getPrice();
                    if (qualifiesForDiscount) {
                        BigDecimal discount = price.multiply(DISCOUNT_PERCENTAGE);
                        return price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
                    }
                    return price;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
        CartEntity savedCart = cartRepository.save(cart);

        CartDTOResponse initialDto = cartResponseMapper.toDTO(savedCart);

        if (qualifiesForDiscount) {
            List<InfoGameDTOResponse> discountedGames = initialDto.games().stream()
                    .map(gameDto -> {
                        BigDecimal originalPrice = gameDto.price();
                        BigDecimal itemDiscount = originalPrice.multiply(DISCOUNT_PERCENTAGE);
                        BigDecimal discountedPrice = originalPrice.subtract(itemDiscount).setScale(2, RoundingMode.HALF_UP);

                        return new InfoGameDTOResponse(
                                gameDto.name(),
                                discountedPrice,
                                gameDto.company()
                        );
                    })
                    .toList();

            return new CartDTOResponse(
                    initialDto.client(),
                    discountedGames,
                    initialDto.totalPrice()
            );
        }

        return initialDto;
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
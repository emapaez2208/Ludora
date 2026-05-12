package ExperienceGroup.Ludora.features.cart.domain;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.ICartRepository;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTORequest;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;


import lombok.AllArgsConstructor;

@AllArgsConstructor

public class CartService implements ICartService {

    private final IMapper<CartEntity,CartDTORequest> requestMapper;
    private final IMapper<CartEntity, CartDTOResponse> responseMapper;
    private final ICartRepository cartRepository;


    @Override
    public CartDTOResponse save(CartDTORequest cartDTORequest) {

        CartEntity cart = requestMapper.toEntity(cartDTORequest);

        cartRepository.save(cart);
        return responseMapper.toDTO(cart);

    }

    @Override
    public void addGameToCart(Long Client, Long gameId) {


    }

    @Override
    public Double PrecioTotal() {
        return 0.0;
    }

    @Override
    public void removeGameFromCart(Long Client, Long gameId) {

    }

    @Override
    public void deleteByClientId(Long clientId) {

    }


}

package ExperienceGroup.Ludora.features.cart.domain.mapper;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.mappers.IClientResponseMapper;
import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ICartResponseMapper extends IMapper<CartEntity, CartDTOResponse> {

    @Override
    CartEntity toEntity(CartDTOResponse cartDTOResponse);

    @Override
    CartDTOResponse toDTO(CartEntity cartEntity);
}
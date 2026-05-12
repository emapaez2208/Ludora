package ExperienceGroup.Ludora.features.cart.domain.mapper;


import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTORequest;
import ExperienceGroup.Ludora.features.client.domain.mappers.IClientResponseMapper;
import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ICartRequestMapper extends IMapper <CartEntity, CartDTORequest> {

    CartEntity toEntity(CartDTORequest cartDTOResquest);
    CartDTORequest toDTO(CartEntity cartEntity);
}

package ExperienceGroup.Ludora.features.cart.domain.mapper;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {IGameResponseMapper.class, IUserResponseMapper.class})
public interface iCartResponseMapper extends IMapper<CartEntity, CartDTOResponse> {

    CartEntity toEntity(CartDTOResponse cartDTOResponse);
    CartDTOResponse toDTO(CartEntity cartEntity);
}

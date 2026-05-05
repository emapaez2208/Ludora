package ExperienceGroup.Ludora.features.cart.domain.mapper;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface iCartResponseMapper extends IMapper<CartEntity, CartDTOResponse> {

    CartEntity toEntity(CartDTOResponse cartDTOResponse);
    CartDTOResponse toDTO(CartEntity cartEntity);
}

package ExperienceGroup.Ludora.features.cart.domain.mapper;


import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartRequestMapper extends IMapper <CartEntity, CartDTORequest> {

    CartEntity toEntity(CartDTORequest cartDTORequest);
    CartDTORequest toDTO(CartEntity cartEntity);
}

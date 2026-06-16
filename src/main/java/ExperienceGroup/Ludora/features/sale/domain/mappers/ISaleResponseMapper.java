package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.mappers.IClientResponseMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.InfoGameDTOResponse;
import ExperienceGroup.Ludora.features.sale.domain.SaleItemEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;

import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleItemDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ISaleResponseMapper extends IMapper<SaleEntity, SaleDTOResponse> {

    SaleEntity toEntity(SaleDTOResponse saleDTOResponse);

    @Mapping(source = "client.userName", target = "userName")
    @Mapping(source = "items", target = "items")
    SaleDTOResponse toDTO(SaleEntity saleEntity);
    default List<SaleItemDTOResponse> itemsToSaleItemDTOResponseList(List<SaleItemEntity> items) {
        if (items == null) return null;
        return items.stream()
                .map(item -> new SaleItemDTOResponse(
                        item.getGame().getName(),
                        item.getGame().getDeveloper().getCompany(),
                        item.getPriceAtSale()
                ))
                .toList();
    }
}

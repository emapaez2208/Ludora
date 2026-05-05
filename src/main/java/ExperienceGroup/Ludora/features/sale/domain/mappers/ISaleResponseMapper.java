package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;


import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import ExperienceGroup.Ludora.features.client.domain.mapper.IClientResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ISaleResponseMapper extends IMapper<SaleEntity, SaleDTOResponse> {

    SaleEntity toEntity(SaleDTOResponse saleDTOResponse);
    SaleDTOResponse toDTO(SaleEntity saleEntity);
}

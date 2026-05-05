package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;

import ExperienceGroup.Ludora.features.game.domain.mappers.IGamesRequestMapper;
import ExperienceGroup.Ludora.features.client.domain.mapper.IClientRequestMapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IGamesRequestMapper.class, IClientRequestMapper.class})
public interface ISaleRequestMapper extends IMapper<SaleEntity, SaleDTORequest> {

    SaleEntity toEntity(SaleDTORequest saleDTORequest);

    SaleDTORequest toDTO(SaleEntity saleEntity);
}


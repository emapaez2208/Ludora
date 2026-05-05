package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.mappers.IClientResponseMapper;
import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ISaleRequestMapper extends IMapper<SaleEntity, SaleDTORequest> {

    SaleEntity toEntity(SaleDTORequest saleDTORequest);

    SaleDTORequest toDTO(SaleEntity saleEntity);
}


package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ISaleRequestMapper extends IMapper<SaleEntity, SaleDTORequest> {
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "games", ignore = true)
    SaleEntity toEntity(SaleDTORequest saleDTORequest);

    @Mapping(target = "clientExternalId", ignore = true)
    @Mapping(target = "gameExternalId", ignore = true)
    SaleDTORequest toDTO(SaleEntity saleEntity);
}


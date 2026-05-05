package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ISaleRequestMapper extends IMapper<SaleEntity, SaleDTORequest> {

    SaleEntity toEntity(SaleDTORequest saleDTORequest);
    SaleDTORequest toDTO(SaleEntity saleEntity);
}


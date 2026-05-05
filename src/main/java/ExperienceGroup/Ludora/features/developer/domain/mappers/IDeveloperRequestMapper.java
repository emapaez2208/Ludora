package ExperienceGroup.Ludora.features.developer.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import org.mapstruct.Mapper;

@Mapper (componentModel ="spring")
public interface IDeveloperRequestMapper extends IMapper<DeveloperEntity, DeveloperDtoRequest> {


    DeveloperEntity toEntity(DeveloperDtoRequest developerDtoRequest);


    DeveloperDtoRequest toDTO(DeveloperEntity developerEntity);
}

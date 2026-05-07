package ExperienceGroup.Ludora.features.developer.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserRequestMapper;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper (componentModel ="spring",uses ={IUserResponseMapper.class})
public interface IDeveloperRequestMapper extends IMapper<DeveloperEntity, DeveloperDtoRequest> {


    DeveloperEntity toEntity(DeveloperDtoRequest developerDtoRequest);


    DeveloperDtoRequest toDTO(DeveloperEntity developerEntity);
}

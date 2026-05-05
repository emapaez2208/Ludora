package ExperienceGroup.Ludora.features.developer.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring", uses ={IUserResponseMapper.class})
public interface IDeveloperResponseMapper extends IMapper<DeveloperEntity, DeveloperDtoResponse> {


    DeveloperEntity toEntity(DeveloperDtoResponse developerDtoResponse);

    DeveloperDtoResponse toDTO(DeveloperEntity developerEntity);
}

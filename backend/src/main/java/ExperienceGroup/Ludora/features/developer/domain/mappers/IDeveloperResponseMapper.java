package ExperienceGroup.Ludora.features.developer.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.common.utils.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface IDeveloperResponseMapper extends IMapper<DeveloperEntity, DeveloperDtoResponse> {


    DeveloperEntity toEntity(DeveloperDtoResponse developerDtoResponse);

    @Mapping(source = "email.value", target = "email")
    DeveloperDtoResponse toDTO(DeveloperEntity developerEntity);

    default Email map(String email) {
    return email == null ? null : new Email(email);
    }
}

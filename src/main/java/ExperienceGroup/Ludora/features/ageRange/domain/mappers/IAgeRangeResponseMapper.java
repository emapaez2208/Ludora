package ExperienceGroup.Ludora.features.ageRange.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAgeRangeResponseMapper extends IMapper<AgeRangeEntity, AgeRangeDTOResponse> {

    AgeRangeEntity toEntity(AgeRangeDTOResponse ageRangeDTOResponse);
    AgeRangeDTOResponse toDTO(AgeRangeEntity ageRangeEntity);
}

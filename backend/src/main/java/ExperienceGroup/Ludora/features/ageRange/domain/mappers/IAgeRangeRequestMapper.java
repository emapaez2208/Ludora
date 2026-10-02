package ExperienceGroup.Ludora.features.ageRange.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAgeRangeRequestMapper extends IMapper<AgeRangeEntity, AgeRangeDTORequest> {

    AgeRangeEntity toEntity(AgeRangeDTORequest ageRangeDTORequest);
    AgeRangeDTORequest toDTO(AgeRangeEntity ageRangeEntity);
}

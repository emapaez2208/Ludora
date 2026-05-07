package ExperienceGroup.Ludora.features.review.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.review.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IReviewResponseMapper extends IMapper<ReviewEntity, ReviewDTOResponse> {

    ReviewEntity toEntity(ReviewDTOResponse reviewDTOResponse);
    ReviewDTOResponse toDTO(ReviewEntity reviewEntity);
}

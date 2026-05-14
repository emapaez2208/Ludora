package ExperienceGroup.Ludora.features.review.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface IReviewRequestMapper extends IMapper<ReviewEntity, ReviewDTORequest> {

    ReviewEntity toEntity(ReviewDTORequest reviewDTORequest);
    ReviewDTORequest toDTO(ReviewEntity reviewDTOEntity);
}

package ExperienceGroup.Ludora.features.review.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IReviewResponseMapper extends IMapper<ReviewEntity, ReviewDTOResponse> {

    ReviewEntity toEntity(ReviewDTOResponse reviewDTOResponse);

    @Mapping(source = "client.userName", target = "userName")
    @Mapping(source = "game.name", target = "gameTitle")
    ReviewDTOResponse toDTO(ReviewEntity reviewEntity);
}

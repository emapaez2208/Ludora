package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IReviewService {

    ReviewDTOResponse save(ReviewDTORequest reviewDTORequest);

    List<ReviewDTOResponse> getAllReviewsByGameID(UUID gameID);

    List<ReviewDTOResponse> getAllReviewsByUserID(UUID id);

    List<ReviewDTOResponse> getAllReviewsByGameIDAndUserID(UUID gameID, UUID id);

    void delete(UUID id);
}

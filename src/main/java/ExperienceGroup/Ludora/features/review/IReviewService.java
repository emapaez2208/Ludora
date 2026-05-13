package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IReviewService {

    ReviewDTOResponse save(ReviewDTORequest reviewDTORequest);

    List<ReviewDTOResponse> getAllReviewsByGameId(UUID gameId);

    List<ReviewDTOResponse> getAllReviewsByClientId(UUID clientId);

    List<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(UUID gameId, UUID clientId);

    void delete(UUID reviewID);

}

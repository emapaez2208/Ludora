package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IReviewService {

    Page<ReviewDTOResponse> getAllReviews(int page,
                                          int size,
                                          UUID gameId,
                                          UUID clientId,
                                          Integer minRating,
                                          Integer maxRating,
                                          LocalDateTime minDate,
                                          LocalDateTime maxDate);

    ReviewDTOResponse save(ReviewDTORequest reviewDTORequest);

    Page<ReviewDTOResponse> getAllReviewsByGameId(int page, int size, UUID gameId);

    Page<ReviewDTOResponse> getAllReviewsByClientId(int page, int size, UUID clientId);

    Page<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(int page, int size, UUID gameId, UUID clientId);

    void delete(UUID reviewID);

}

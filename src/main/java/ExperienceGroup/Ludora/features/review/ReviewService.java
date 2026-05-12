package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.common.exception.ReviewNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService {
    private final IReviewRepository reviewRepository;
    private final IMapper<ReviewEntity, ReviewDTOResponse> responseMapper;
    private final IMapper<ReviewEntity, ReviewDTORequest> requestMapper;

    @Override
    public ReviewDTOResponse save(ReviewDTORequest reviewDTORequest) {
        ReviewEntity reviewEntity = requestMapper.toEntity(reviewDTORequest);
        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);

        return responseMapper.toDTO(savedReviewEntity);
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameID(UUID gameID) {
        List<ReviewEntity> reviews = reviewRepository.findByGameID(gameID);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByUserID(UUID userID) {
        List<ReviewEntity> reviews = reviewRepository.findByUserID(userID);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameIDAndUserID(UUID gameID, UUID userID) {
        List<ReviewEntity> reviews = reviewRepository.findByGameIDAndUserID(gameID, userID);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(UUID reviewID) {
        ReviewEntity review = reviewRepository.findByExternalId(reviewID)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }
}

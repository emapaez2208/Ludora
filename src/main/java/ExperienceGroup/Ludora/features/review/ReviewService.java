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

    // servicios IGameService gameService;
    // servicios IClientService clientService;

    @Override
    public ReviewDTOResponse save(ReviewDTORequest reviewDTORequest) {
        //buscar el juego usando findbyexternalid de servicio juego
        //buscar el cliente usando findbyexternalid de servicio cliente

        ReviewEntity reviewEntity = requestMapper.toEntity(reviewDTORequest);

        //setear game usando setGame()
        //setear cliente usando setClient()

        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);

        return responseMapper.toDTO(savedReviewEntity);
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameId(UUID gameId) {
        List<ReviewEntity> reviews = reviewRepository.findByGameExternalId(gameId);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByClientId(UUID clientId) {
        List<ReviewEntity> reviews = reviewRepository.findByClientExternalId(clientId);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(UUID gameId, UUID clientId) {
        List<ReviewEntity> reviews = reviewRepository.findByGameExternalIdAndClientExternalId(gameId, clientId);

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

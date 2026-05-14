package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.common.exception.ReviewNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import jakarta.transaction.Transactional;
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

    private final IGameService gameService;
    private final IClientService clientService;

    @Transactional
    @Override
    public ReviewDTOResponse save(ReviewDTORequest reviewDTORequest) {
        GameEntity game = gameService.getByExternalId(reviewDTORequest.gameExternalId());
        ClientEntity client = clientService.getByExternalID(reviewDTORequest.clientExternalId());

        ReviewEntity reviewEntity = requestMapper.toEntity(reviewDTORequest);

        reviewEntity.setGame(game);
        reviewEntity.setClient(client);

        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);

        return responseMapper.toDTO(savedReviewEntity);
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameId(UUID gameId) {

        GameEntity game = gameService.getByExternalId(gameId);
        List<ReviewEntity> reviews = reviewRepository.findByGame(game);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByClientId(UUID clientId) {
        ClientEntity client = clientService.getByExternalID(clientId);
        List<ReviewEntity> reviews = reviewRepository.findByClient(client);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(UUID gameId, UUID clientId) {
        GameEntity game = gameService.getByExternalId(gameId);
        ClientEntity client = clientService.getByExternalID(clientId);

        List<ReviewEntity> reviews = reviewRepository.findByGameAndClient(game, client);

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

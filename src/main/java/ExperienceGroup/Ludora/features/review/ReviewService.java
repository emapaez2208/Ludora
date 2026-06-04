package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.review.exception.ReviewNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService {
    private final IReviewRepository reviewRepository;
    private final IMapper<ReviewEntity, ReviewDTOResponse> responseMapper;
    private final IMapper<ReviewEntity, ReviewDTORequest> requestMapper;

    private final IGameRepository gameRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<ReviewDTOResponse> getAllReviews(UUID gameId,
                                                 UUID clientId,
                                                 Integer minRating,
                                                 Integer maxRating,
                                                 LocalDateTime minDate,
                                                 LocalDateTime maxDate) {

        PredicateSpecification<ReviewEntity> spec = PredicateSpecification.allOf(
                ReviewSpecification.gameEquals(gameId),
                ReviewSpecification.clientEquals(clientId),
                ReviewSpecification.ratingBetween(minRating, maxRating),
                ReviewSpecification.dateBetween(minDate, maxDate)
        );

        List<ReviewEntity> reviews = reviewRepository.findAll(spec);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAuthority('CREATE_REVIEW')")
    @Override
    public ReviewDTOResponse save(ReviewDTORequest reviewDTORequest) {
        GameEntity game = gameRepository.findByExternalId(reviewDTORequest.gameExternalId())
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
        ClientEntity client = clientRepository.findByExternalId(reviewDTORequest.clientExternalId())
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        ReviewEntity reviewEntity = requestMapper.toEntity(reviewDTORequest);

        reviewEntity.setGame(game);
        reviewEntity.setClient(client);

        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);

        return responseMapper.toDTO(savedReviewEntity);
    }

    @Override
    public List<ReviewDTOResponse> getAllReviewsByGameId(UUID gameId) {
        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        List<ReviewEntity> reviews = reviewRepository.findByGame(game);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.externalId")
    public List<ReviewDTOResponse> getAllReviewsByClientId(UUID clientId) {
        ClientEntity client = clientRepository.findByExternalId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        List<ReviewEntity> reviews = reviewRepository.findByClient(client);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.externalId")
    public List<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(UUID gameId, UUID clientId) {
        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        ClientEntity client = clientRepository.findByExternalId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<ReviewEntity> reviews = reviewRepository.findByGameAndClient(game, client);

        return reviews.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_REVIEW')")
    public void delete(UUID reviewID) {
        ReviewEntity review = reviewRepository.findByExternalId(reviewID)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }
}

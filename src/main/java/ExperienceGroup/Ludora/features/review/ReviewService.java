package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Page<ReviewDTOResponse> getAllReviews(int page,
                                                 int size,
                                                 UUID gameId,
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

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<ReviewEntity> reviews = reviewRepository.findAll(Specification.where(spec), pageable);

        return reviews
                .map(responseMapper::toDTO);
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
    public Page<ReviewDTOResponse> getAllReviewsByGameId(int page, int size, UUID gameId) {
        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<ReviewEntity> reviews = reviewRepository.findByGame(game, pageable);

        return reviews
                .map(responseMapper::toDTO);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.externalId")
    public Page<ReviewDTOResponse> getAllReviewsByClientId(int page, int size, UUID clientId) {
        ClientEntity client = clientRepository.findByExternalId(clientId)
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<ReviewEntity> reviews = reviewRepository.findByClient(client, pageable);

        return reviews
                .map(responseMapper::toDTO);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.externalId")
    public Page<ReviewDTOResponse> getAllReviewsByGameIdAndClientId(int page, int size, UUID gameId, UUID clientId) {
        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
        ClientEntity client = clientRepository.findByExternalId(clientId)
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<ReviewEntity> reviews = reviewRepository.findByGameAndClient(game, client, pageable);

        return reviews
                .map(responseMapper::toDTO);
    }



    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    public void delete(UUID reviewID) {

        ReviewEntity review = reviewRepository.findByExternalId(reviewID)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));       // traigo la review

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));  ///compruebo si es admin

        if (!isAdmin) {
            UUID currentUser = ((CredentialsEntity) SecurityContextHolder.getContext()         ///  si no es admin
                    .getAuthentication().getPrincipal()).getExternalId();

            if (!review.getClient().getExternalId().equals(currentUser)) {                        /// compruebo , es o no su review?
                throw new AccessDeniedException("without sufficient permissions");   /// si no es largo excepcion
            }
        }

        reviewRepository.delete(review);
    }

}

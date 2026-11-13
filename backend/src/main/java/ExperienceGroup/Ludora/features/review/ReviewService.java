package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.providers.AuthenticatedUserProvider;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.review.exception.GameNotPurchasedException;
import ExperienceGroup.Ludora.features.review.exception.ReviewAlreadyExistsException;
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
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOUpdate;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOSummary;
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
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService {
    private static final List<String> SORT_FIELDS = List.of("date", "rating");
    private final IReviewRepository reviewRepository;
    private final IMapper<ReviewEntity, ReviewDTOResponse> responseMapper;
    private final IMapper<ReviewEntity, ReviewDTORequest> requestMapper;
    private final AuthenticatedUserProvider authenticatedUser;

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
                                                 LocalDateTime maxDate,
                                                 String userName,
                                                 String sortBy,
                                                 String sortDirection) {

        if (minRating != null && (minRating < 1 || minRating > 5)) {
            throw new IllegalArgumentException("Minimum rating must be between 1 and 5.");
        }
        if (maxRating != null && (maxRating < 1 || maxRating > 5)) {
            throw new IllegalArgumentException("Maximum rating must be between 1 and 5.");
        }
        if (minRating != null && maxRating != null && minRating > maxRating) {
            throw new IllegalArgumentException("Minimum rating cannot be greater than maximum rating.");
        }

        LocalDateTime now = LocalDateTime.now();

        if (minDate != null && maxDate != null && minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("The start date cannot be later than the end date.");
        }

        if (minDate != null && minDate.isAfter(now)) {
            throw new IllegalArgumentException("The minimum date cannot be in the future.");
        }
        if (maxDate != null && maxDate.isAfter(now)) {
            throw new IllegalArgumentException("The maximum date cannot be in the future.");
        }
        if (sortBy == null || !SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field. Allowed: date, rating.");
        }

        PredicateSpecification<ReviewEntity> spec = PredicateSpecification.allOf(
                ReviewSpecification.gameEquals(gameId),
                ReviewSpecification.clientEquals(clientId),
                ReviewSpecification.ratingBetween(minRating, maxRating),
                ReviewSpecification.dateBetween(minDate, maxDate),
                ReviewSpecification.userNameContains(userName)
        );
        
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);
        if (!sortBy.equals("date")) {
            // desempate: ante igual calificación, la más reciente primero
            sort = sort.and(Sort.by(Sort.Direction.DESC, "date"));
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewEntity> reviews = reviewRepository.findAll(Specification.where(spec), pageable);

        return reviews
                .map(responseMapper::toDTO);
    }

    @Override
    public ReviewDTOSummary getSummaryByGameId(UUID gameId) {
        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        Double average = reviewRepository.findAverageRatingByGame(game);
        long total = reviewRepository.countByGame(game);

        //si el juego no tiene reseñas, 0.0
        double rounded = (average == null) ? 0.0 : Math.round(average * 10.0) / 10.0;

        return new ReviewDTOSummary(rounded, total);
    }

    @Transactional
    @PreAuthorize("hasAuthority('CREATE_REVIEW')")
    @Override
    public ReviewDTOResponse save(ReviewDTORequest reviewDTORequest) {

        GameEntity game = gameRepository.findByExternalId(reviewDTORequest.gameExternalId())
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        UUID authenticatedClientId = authenticatedUser.getCurrentUser().externalId();

        if (!reviewDTORequest.clientExternalId().equals(authenticatedClientId)) {
            throw new AccessDeniedException("You can only create reviews for your own account.");
        }

        ClientEntity client = clientRepository.findByExternalId(reviewDTORequest.clientExternalId())
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        boolean hasPurchased = client.getGames().stream()
                .anyMatch(purchasedGame -> purchasedGame.getExternalId().equals(game.getExternalId()));

        if (!hasPurchased) {
            throw new GameNotPurchasedException("You cannot review a game that you have not purchased.");
        }

        boolean alreadyReviewed = reviewRepository.existsByClientAndGame(client, game);
        if (alreadyReviewed) {
            throw new ReviewAlreadyExistsException("You have already reviewed this game.");
        }

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
    @PreAuthorize("hasRole('CLIENT')")
    public ReviewDTOResponse update(UUID reviewId, ReviewDTOUpdate reviewDTOUpdate) {

    ReviewEntity review = reviewRepository.findByExternalId(reviewId)
            .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

    UUID currentUser = authenticatedUser.getCurrentUser().externalId();

    // solo el autor puede editar su reseña (el admin solo puede eliminar)
    if (!review.getClient().getExternalId().equals(currentUser)) {
        throw new AccessDeniedException("You can only edit your own reviews.");
    }

    review.setRating(reviewDTOUpdate.rating());
    review.setComment(reviewDTOUpdate.comment());

    return responseMapper.toDTO(reviewRepository.save(review));
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
            UUID currentUser = authenticatedUser.getCurrentUser().externalId();

            if (!review.getClient().getExternalId().equals(currentUser)) {                        /// compruebo , es o no su review?
                throw new AccessDeniedException("without sufficient permissions");   /// si no es largo excepcion
            }
        }

        reviewRepository.delete(review);
    }

}

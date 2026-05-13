package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long> {

    Optional<ReviewEntity> findByExternalId(UUID externalId);

    List<ReviewEntity> findByClientExternalId(UUID clientId);

    List<ReviewEntity> findByGameExternalId(UUID gameId);

    List<ReviewEntity> findByGameExternalIdAndClientExternalId(UUID gameId, UUID clientId);

    List<ReviewEntity> findByRatingGreaterThan(int minRating);

    List<ReviewEntity> findByRatingLessThan(int maxRating);

    List<ReviewEntity> findByDate(LocalDateTime date);

}

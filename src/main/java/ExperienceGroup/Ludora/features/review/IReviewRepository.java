package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long> {

    List<ReviewEntity> findByRatingGreaterThan(int minRating);

    List<ReviewEntity> findByRatingLessThan(int maxRating);

    List<ReviewEntity> findByUserID(UUID userId);

    List<ReviewEntity> findByDate(LocalDateTime date);

    Optional<ReviewEntity> findByExternalId(UUID externalId);

    List<ReviewEntity> findByGameID(UUID gameID);

    List<ReviewEntity> findByGameIDAndUserID(UUID gameID, UUID userID);


}

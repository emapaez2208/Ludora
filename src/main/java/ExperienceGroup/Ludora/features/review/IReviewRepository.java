package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long> {

    Optional<ReviewEntity> findByRatingGreaterThan(int minRating);

    Optional<ReviewEntity> findByRatingLessThan(int maxRating);

    Optional<ReviewEntity> findByUserId(Long userId);

    Optional<ReviewEntity> findByDate(LocalDateTime date);


}

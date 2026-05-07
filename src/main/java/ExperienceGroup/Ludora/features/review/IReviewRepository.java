package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long> {

    List<ReviewEntity> findByRatingGreaterThan(int minRating);

    List<ReviewEntity> findByRatingLessThan(int maxRating);

    List<ReviewEntity> findByUserId(Long userId);

    List<ReviewEntity> findByDate(LocalDateTime date);


}

package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAgeRangeRepository extends JpaRepository<AgeRangeEntity, Long> {

    Optional<AgeRangeEntity> findByAgeRange(String ageRange);

    Optional<AgeRangeEntity> findByAgeLimit(int ageLimit);
}

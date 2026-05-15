package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAgeRangeRepository extends JpaRepository<AgeRangeEntity, Long> {

    Optional<AgeRangeEntity> findByExternalId(UUID externalId);

    List<AgeRangeEntity> findByRangeName(String rangeName);

    List<AgeRangeEntity> findByMinAge(Integer minAge);

}

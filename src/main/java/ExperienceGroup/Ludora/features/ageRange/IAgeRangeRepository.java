package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAgeRangeRepository extends JpaRepository<AgeRangeEntity, Long> {

    List<AgeRangeEntity> findByRangeName(String rangeName);

    List<AgeRangeEntity> findByMinAge(Integer minAge);

}

package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IGameRepository extends JpaRepository<GameEntity, Long>, JpaSpecificationExecutor<GameEntity>{

    Optional<GameEntity> findByNameIgnoreCase(String name);

    Optional<GameEntity> findByExternalId(UUID externalId);

    List<GameEntity> findByGenres_NameIgnoreCase(String name);

    List<GameEntity> findByAgeRange_RangeNameIgnoreCase(String ageRange);

    Optional<List<GameEntity>> findAllByClients_ExternalId(UUID externalId);

    List<GameEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<GameEntity> findByStatusBlocked(Boolean statusBlocked);

    List<GameEntity> findByReleaseDateBetween(LocalDate minDate, LocalDate maxDate);

    Optional<List<GameEntity>> findAllByNeedRevision(Boolean needRevision);

}

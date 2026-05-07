package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IGameRepository extends JpaRepository<GameEntity, Long> {

    Optional<GameEntity> findByNameIgnoreCase(String name);

    Optional<GameEntity> findByExternalId(UUID externalId);

    List<GameEntity> findByGenres_NameIgnoreCase(String name);

    List<GameEntity> findByAgeRange_RangeNameIgnoreCase(String ageRange);

    //List<GameEntity> findByUsers_UserNameIgnoreCase(String userName);

    List<GameEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<GameEntity> findByStatusBlocked(Boolean statusBlocked);

    List<GameEntity> findByReleaseDateBetween(LocalDate minDate, LocalDate maxDate);

}

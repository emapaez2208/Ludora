package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    List<GameEntity> findAllByClients_ExternalId(UUID externalId);

    List<GameEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<GameEntity> findByStatusBlocked(Boolean statusBlocked);

    List<GameEntity> findByReleaseDateBetween(LocalDate minDate, LocalDate maxDate);

    Optional<List<GameEntity>> findAllByNeedRevision(Boolean needRevision);

    boolean existsByGenresContaining(GenreEntity genre);

    boolean existsByAgeRange(AgeRangeEntity ageRange);

        // Juegos visibles ordenados por cantidad de clientes que los tienen en su biblioteca
    @Query("""
            select g from GameEntity g
            left join g.clients c
            where g.statusBlocked = false and g.needRevision = false
            group by g
            order by count(c) desc, g.name asc
            """)
    List<GameEntity> findMostPopular(Pageable pageable);

    // Juegos visibles con al menos :minReviews reseñas, ordenados por promedio y luego por cantidad
    @Query("""
            select r.game from ReviewEntity r
            where r.game.statusBlocked = false and r.game.needRevision = false
            group by r.game
            having count(r) >= :minReviews
            order by avg(r.rating) desc, count(r) desc
            """)
    List<GameEntity> findTopRated(@Param("minReviews") long minReviews, Pageable pageable);

}

package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISaleRepository extends JpaRepository<SaleEntity, Long> {
    Optional<SaleEntity> findByExternalId(UUID externalId);

    List<SaleEntity> findByStatus(ESaleStatus status);

    List<SaleEntity> findByDate(LocalDateTime date);

    List<SaleEntity> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<SaleEntity> findByClientExternalId(UUID clientExternalId);
}
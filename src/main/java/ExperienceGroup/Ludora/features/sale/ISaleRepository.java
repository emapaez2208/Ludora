package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByStatus(String status);

    List<SaleEntity> findByDate(LocalDateTime date);

    List<SaleEntity> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<SaleEntity> findByClientId(Long clientId);
}
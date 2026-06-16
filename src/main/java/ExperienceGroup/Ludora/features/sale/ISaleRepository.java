package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISaleRepository extends JpaRepository<SaleEntity, Long>, JpaSpecificationExecutor<SaleEntity> {
    Optional<SaleEntity> findByExternalId(UUID externalId);

    Page<SaleEntity> findByClient(ClientEntity client, Pageable pageable);

}
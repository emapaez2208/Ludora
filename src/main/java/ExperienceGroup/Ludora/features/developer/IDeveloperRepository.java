package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IDeveloperRepository extends JpaRepository<DeveloperEntity, Long>, JpaSpecificationExecutor<DeveloperEntity> {

    Optional<DeveloperEntity> findByExternalId(UUID externalId);

    Optional<DeveloperEntity> findBycompany(String company);

}

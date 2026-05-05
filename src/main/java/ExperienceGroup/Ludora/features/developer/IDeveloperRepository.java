package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IDeveloperRepository extends JpaRepository<DeveloperEntity, Long>{

    Optional<DeveloperEntity> findByNname(String name);

    Optional<DeveloperEntity> findById(UUID externalId);

    Optional<DeveloperEntity> findBycompany(String company);
    
}

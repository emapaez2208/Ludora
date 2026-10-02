package ExperienceGroup.Ludora.auth.credentials;

import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {

    Optional<CredentialsEntity> findByUsername(String username);

    Optional<CredentialsEntity> findByExternalId(UUID Id);
}

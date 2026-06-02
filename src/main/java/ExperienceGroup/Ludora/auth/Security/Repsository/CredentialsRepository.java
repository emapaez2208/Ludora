package ExperienceGroup.Ludora.auth.Security.Repsository;

import ExperienceGroup.Ludora.auth.Security.Credential.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity,Long> {

    Optional<CredentialsEntity> findByUsername (String username);
}

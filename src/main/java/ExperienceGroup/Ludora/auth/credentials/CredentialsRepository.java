package ExperienceGroup.Ludora.auth.credentials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {

}

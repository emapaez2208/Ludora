package ExperienceGroup.Ludora.auth.credentials;

import com.zaxxer.hikari.util.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<CredentialsEntity> findByUsername(String username);
}

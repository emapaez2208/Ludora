package ExperienceGroup.Ludora.auth.credentials;

import com.zaxxer.hikari.util.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<CredentialsEntity> findByUsername(String username);
}

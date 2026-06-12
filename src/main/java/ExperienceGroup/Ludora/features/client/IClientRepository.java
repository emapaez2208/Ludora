package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClientRepository extends JpaRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {
    Optional<ClientEntity> findByUserName(String userName);

    Optional<ClientEntity> findByExternalId(UUID externalId);

    Boolean existsByEmail(Email email);

    Boolean existsByUserName(String userName);

    Optional<ClientEntity> findByStatusBlocked(Boolean statusBlocked);
    
}

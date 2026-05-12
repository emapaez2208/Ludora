package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClientRepository extends JpaRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {
    Optional<ClientEntity> findByUserName(String userName);

    Optional<ClientEntity> findByExternalId(UUID externalId);

    Optional<ClientEntity> findByNameAndLastName(String name, String lastName);

    Optional<ClientEntity> findByStatusBlocked(Boolean statusBlocked);
    
}

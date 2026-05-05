package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface    IClientRepository extends JpaRepository<ClientEntity,Long> {
    
}

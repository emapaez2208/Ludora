package ExperienceGroup.Ludora.features.user;

import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByExternalId(UUID externalId);

    Optional<UserEntity> findByNameAndLastName(String name, String lastName);

    Optional<UserEntity> findByStatusBlocked(Boolean statusBlocked);
}

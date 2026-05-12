package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IAdminRepository extends JpaRepository<AdminEntity, Long>, JpaSpecificationExecutor<AdminEntity> {

    Optional<AdminEntity> findByEmployeeId (Long employeeId);

    Optional<AdminEntity> findByUserName(String userName);

    Optional<AdminEntity> findByExternalId(UUID externalId);

    Optional<AdminEntity> findByNameAndLastName(String name, String lastName);

    Optional<AdminEntity> findByStatusBlocked(Boolean statusBlocked);
}

package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAdminRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByEmployeeId (Long employeeId);

    Optional<AdminEntity> findByUser(Long user);

    void deleteByUser(UserEntity userEntity);
}

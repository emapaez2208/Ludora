package ExperienceGroup.Ludora.features.role;

import ExperienceGroup.Ludora.features.role.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository <RoleEntity,Long>{
    Optional<RoleEntity> findByName (String name);
}

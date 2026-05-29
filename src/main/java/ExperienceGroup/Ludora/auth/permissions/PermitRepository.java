package ExperienceGroup.Ludora.auth.permissions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository extends JpaRepository<PermitEntity, Long> {
    PermitEntity Permit(Permits permit);

}

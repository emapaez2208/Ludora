package ExperienceGroup.Ludora.auth.Security.Entity.Role;

import ExperienceGroup.Ludora.auth.Security.Entity.Permit.PermitEntity;
import ExperienceGroup.Ludora.auth.Security.Enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    @Column(nullable = false,unique = true)
    private Roles role;

    @ManyToMany(cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinTable(
            name="role_permits",
            joinColumns = @JoinColumn ( name = "role_id"),
            inverseJoinColumns = @JoinColumn ( name = "permit_id"))
    private final Set<PermitEntity> permit = new HashSet<>();

    public RoleEntity (Roles name){
        this.role = name;
    }

    public void addPermits ( PermitEntity permitEntity){
        this.permit.add(permitEntity);
    }

}

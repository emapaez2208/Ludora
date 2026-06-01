package ExperienceGroup.Ludora.auth.Security.Entity.Permit;

import ExperienceGroup.Ludora.auth.Security.Enums.Permits;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor @Builder
public class PermitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , unique = true)
    private Permits permit;

}

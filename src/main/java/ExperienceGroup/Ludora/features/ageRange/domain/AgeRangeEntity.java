package ExperienceGroup.Ludora.features.ageRange.domain;

import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "age_ranges")
public class AgeRangeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Column(name = "range_name", nullable = false, length = 50, unique = true)
    private String rangeName;

    @Column (name = "min_age", nullable = false)
    private Integer minAge;

    @Column(name = "description", length = 100)
    private String description;


    @OneToMany(mappedBy = "ageRange")
    private List<GameEntity> games;

    @PrePersist
    void onCreate() {
        if (externalId == null)
            externalId = UUID.randomUUID();
    }

}

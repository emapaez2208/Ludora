package ExperienceGroup.Ludora.features.ageRange.domain;

import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Column(name = "range_name", nullable = false, length = 50)
    private String rangeName;

    @Column (name = "min_age", nullable = false)
    private Integer minAge;

    @Column(name = "description", length = 100)
    private String description;


    @OneToMany(mappedBy = "ageRange")
    private List<GameEntity> games;

}

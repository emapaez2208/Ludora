package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.game.GameEntity;
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

    @Column(name = "age_range", nullable = false, length = 50)
    private String ageRange;

    @Column (name = "age_limit", nullable = false)
    private int ageLimit;


    @OneToMany(mappedBy = "ageRange")
    private List<GameEntity> games;

}

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
    private Long age_range_id;

    @Column(nullable = false, length = 50)
    private String age_range;

    @Column (nullable = false)
    private int age_limit;


    @OneToMany(mappedBy = "age_range")
    private List<GameEntity> games;

}

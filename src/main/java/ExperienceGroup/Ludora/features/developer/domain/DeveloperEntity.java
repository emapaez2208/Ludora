package ExperienceGroup.Ludora.features.developer.domain;


import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "developers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeveloperEntity extends UserEntity {

    @OneToMany (mappedBy = "developer")
    private List<GameEntity> games;

    @Column(nullable = false)
    private String company;

}

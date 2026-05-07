package ExperienceGroup.Ludora.features.developer.domain;


import ExperienceGroup.Ludora.features.game.GameEntity;
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

public class DeveloperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private UserEntity user;

    @ManyToOne (mappedBy = "developers")
    private List<GameEntity> games;

    @Column(nullable = false)
    private String company;

}

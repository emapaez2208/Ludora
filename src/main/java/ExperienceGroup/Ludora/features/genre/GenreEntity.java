package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.game.GameEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name= "Genres")
@Getter
@Setter
@AllArgsConstructor
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String descripcion;

    @ManyToMany
    @JoinTable(
            name= " Genre_Game",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name="game_id")

    )
    private List<GameEntity> gameEntities ;

}

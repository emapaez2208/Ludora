package ExperienceGroup.Ludora.features.cart;


import ExperienceGroup.Ludora.features.game.GameEntity;
import ExperienceGroup.Ludora.features.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Table(name = "cart")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity users;

    @ManyToMany
    @JoinTable(
            name = "carts_games",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<GameEntity> games;


    private Double total_price= 0.0;

}

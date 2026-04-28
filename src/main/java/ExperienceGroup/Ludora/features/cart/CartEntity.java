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

    @Column (name = total_price)
    private Double total_price= 0.0;

}

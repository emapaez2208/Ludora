package ExperienceGroup.Ludora.features.client.domain;

import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity extends UserEntity {

    @Column(nullable = false)
    private Long phone;

    @Column(nullable = false , length = 20)
    private String street;

    @Column(nullable = false)
    private Integer numberStreet;

    @Column(nullable = false)
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(
            name= "clients_games",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name="game_id")
    )
    private List<GameEntity> games;

    @OneToOne(mappedBy="client",
                cascade=CascadeType.ALL,
              orphanRemoval = true)
    private CartEntity cart;

    private Integer points;

    @PrePersist
    void onCreate(){
        if(games == null){
            games = new ArrayList<>();
        }
    }

}

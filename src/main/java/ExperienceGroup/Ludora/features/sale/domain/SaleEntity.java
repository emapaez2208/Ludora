package ExperienceGroup.Ludora.features.sale.domain;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.GameEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "sale")
public class SaleEntity{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double totalPrice = 0.0;

    @ManyToOne
    @JoinColumn (name = "client_id")
    private ClientEntity client;

    @ManyToMany
    @JoinTable(
            name = "sales_games",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<GameEntity> games;

    @PrePersist
    void OnCreate(){
        if (date == null)
            date = LocalDateTime.now();

    }

}

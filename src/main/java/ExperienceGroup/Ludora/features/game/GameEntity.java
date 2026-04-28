package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.ageRange.AgeRangeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long game_id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_range_id", nullable = false)
    private AgeRangeEntity age_range;

    @Column (length = 200)
    private String description;

    @Column (nullable = false)
    private LocalDate release_date;

    @Column (nullable = false)
    private boolean status_blocked;

    
    @ManyToMany(mappedBy = "gameEntities")
    private List<GenreEntity> genres;

    @OneToMany(mappedBy = "game")
    private List<ReviewEntity> reviews;

    @ManyToMany(mappedBy = "gameEntities")
    private List<CartEntity> carts;

    @ManyToMany(mappedBy = "games")
    private List<SaleEntity> sales;

    // falta mappedBy con users



    @PrePersist
    void onCreate() {
        if (externalId == null)
            externalId = UUID.randomUUID();
    }




}

package ExperienceGroup.Ludora.features.game.domain;

import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private DeveloperEntity developer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_range_id", nullable = false)
    private AgeRangeEntity ageRange;

    @Column (length = 200)
    private String description;

    @Column (name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column (name = "status_blocked", nullable = false)
    private Boolean statusBlocked;

    @Column(nullable = false, name = "need_revision")
    private Boolean needRevision;
    
    @ManyToMany
    @JoinTable(
            name = "games_genres",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres;

    @ManyToMany (mappedBy = "games")
    private List<ClientEntity> clients;

    @PrePersist
    void onCreate() {
        if (externalId == null)
            externalId = UUID.randomUUID();
        if (statusBlocked == null)
            statusBlocked = true;
        if(needRevision == null)
            needRevision = true;
    }

}

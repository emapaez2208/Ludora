package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.game.GameEntity;
import ExperienceGroup.Ludora.features.user.UserEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column(nullable = false)
    private int rating;

    private String comment;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}

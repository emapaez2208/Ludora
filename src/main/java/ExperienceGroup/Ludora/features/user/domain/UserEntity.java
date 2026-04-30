package ExperienceGroup.Ludora.features.user.domain;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import ExperienceGroup.Ludora.features.game.GameEntity;
import ExperienceGroup.Ludora.features.role.RoleEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email_adress", unique = true, nullable = false))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password",nullable = false))
    private Password password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(nullable = false)
    private Boolean statusBlocked;

    @ManyToMany
    @JoinTable(
            name= "users_games",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="game_id")
    )
    private List<GameEntity> games;

    @PrePersist
    void OnCreate(){
        if (externalId == null)
            externalId = UUID.randomUUID();

        if(statusBlocked == null)
            statusBlocked = true;
    }



}

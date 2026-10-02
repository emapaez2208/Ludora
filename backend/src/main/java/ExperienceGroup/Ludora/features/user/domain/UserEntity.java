package ExperienceGroup.Ludora.features.user.domain;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity {

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
    @AttributeOverride(name = "value", column = @Column(name = "email_address", unique = true, nullable = false))
    private Email email;

    @Column(nullable = false)
    private Boolean statusBlocked;

    @PrePersist
    void OnCreate(){
        if (externalId == null)
            externalId = UUID.randomUUID();

        if(statusBlocked == null)
            statusBlocked = true;
    }

}

package ExperienceGroup.Ludora.features.Developers;


import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "developers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DevelopersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String company;



}

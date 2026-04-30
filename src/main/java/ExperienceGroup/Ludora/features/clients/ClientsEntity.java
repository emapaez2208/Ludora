package ExperienceGroup.Ludora.features.clients;

import ExperienceGroup.Ludora.features.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false )
    private int phone;

   @Column(nullable = false , length = 20)
    private String street;

   @Column(nullable = false)
    private int numberStreet;

   @FutureOrPresent(message = "La fecha de cumpleanios no puede ser pasada a la fecha actual")
    private LocalDate birhDate;

   @OneToOne
   @JoinColumn(name= "user_id")
    private UserEntity user;
}

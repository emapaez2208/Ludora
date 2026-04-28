package ExperienceGroup.Ludora.features.clients;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

@Entity
@Table(name = "Clients")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @
    private int Phone;

   @NotNull
    private String street;
   @NotNull
    private int numberStreet;

   @Past ( message = "Fecha de nacimiento incorrecta")
    private LocalDate birhdate;
}

package ExperienceGroup.Ludora.features.clients;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private int Phone;

    @Column(NotNull = )
    private String street;

    private int numberStreet;

    private LocalDate birhdate;
}

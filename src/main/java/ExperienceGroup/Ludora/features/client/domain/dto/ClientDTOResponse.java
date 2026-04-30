package ExperienceGroup.Ludora.features.client.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTOResponse {

    private int phone;

    private String street;


    private int numberStreet;

    private LocalDate birhDate;

}

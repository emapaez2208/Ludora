package ExperienceGroup.Ludora.features.client.domain.dto;

import java.time.LocalDate;

public record ClientDTOResponse ( int phone,
                                  String street,
                                  int numberStreet,
                                   LocalDate birhDate)
{

}

package ExperienceGroup.Ludora.features.client.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

import java.time.LocalDate;

public record ClientDTOResponse (int phone,
                                 String street,
                                 int numberStreet,
                                 LocalDate birhDate,
                                 UserDTOResponse user)
{

}

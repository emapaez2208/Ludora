package ExperienceGroup.Ludora.features.client.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

import java.time.LocalDate;
import java.util.UUID;

public record ClientDTOResponse (String name,
                                 String lastName,
                                 String userName,
                                 Email email ,
                                 Boolean statusBlocked ,
                                 int phone,
                                 String street,
                                 int numberStreet,
                                 LocalDate birthDate)
{ }

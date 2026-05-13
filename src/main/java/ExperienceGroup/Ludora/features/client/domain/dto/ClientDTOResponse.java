package ExperienceGroup.Ludora.features.client.domain.dto;

import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

import java.time.LocalDate;
import java.util.UUID;

public record ClientDTOResponse (UUID externalId,
                                 String name,
                                 String lastName,
                                 String userName,
                                 int phone,
                                 String street,
                                 int numberStreet,
                                 LocalDate birthDate)
{ }

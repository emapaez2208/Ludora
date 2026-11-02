package ExperienceGroup.Ludora.features.client.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ClientDTOResponse (UUID externalId,
                                 String name,
                                 String lastName,
                                 String userName,
                                 String email,
                                 Long phone,
                                 String street,
                                 Integer numberStreet,
                                 LocalDate birthDate,
                                 Integer points,
                                 Boolean statusBlocked)
{ }

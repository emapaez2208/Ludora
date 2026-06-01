package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IClientService {
    List<ClientDTOResponse> getAllClient (String name,
                                          String lastName,
                                          String email,
                                          Boolean statusBlocked,
                                          Integer phone,
                                          String street,
                                          Integer numberStreet,
                                          LocalDate birthDate
                                          );

    ClientDTOResponse getByExternalID(UUID externalID);

    ClientDTOResponse getByUserName (String userName);

    ClientDTOResponse save (ClientDTORequest clientDTORequest);

    ClientDTOResponse update (UUID id , ClientDTORequest clientDTORequest);

    void delete (UUID externalID);
}

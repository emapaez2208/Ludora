package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IClientService {
    List<ClientDTOResponse> getAllClient (String name,
                                          String lastName,
                                          String userName,
                                          String email,
                                          Boolean statusBlocked,
                                          Integer phone,
                                          String street,
                                          Integer numberStreet,
                                          LocalDate birthDate
                                          );

    ClientDTOResponse getByExternalID(UUID externalID);

    ClientDTOResponse getByUserName (String userName);

    ClientDTOResponse getMyPerfil();

    ClientDTOResponse save (ClientDTORequest clientDTORequest);

    ClientDTOResponse update (UUID id , ClientUpdateRequest request);

    void delete (UUID externalID);

    List<GameDTOResponse> getMyGames();
}

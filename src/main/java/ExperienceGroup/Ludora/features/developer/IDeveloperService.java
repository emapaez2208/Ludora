package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IDeveloperService {

    List<DeveloperDtoResponse> getAllDevelopers(String name,
                                                String lastName,
                                                String userName,
                                                String email,
                                                Boolean statusBlocked,
                                                String company);

    DeveloperDtoResponse getByExternalId(UUID externalId);

    DeveloperDtoResponse save(DeveloperDtoRequest developerDtoRequest);

    DeveloperDtoResponse update(UUID externalId, DeveloperUpdateRequest developerDtoRequest);

    List<GameDTOResponse> getGamesByDeveloper(UUID externalId);

    void delete(UUID externalId);
}
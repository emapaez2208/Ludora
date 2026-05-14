package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IGameService {

    List<GameDTOResponse> getAllGames();
    GameDTOResponse getByExternalId (UUID externalId);
    GameDTOResponse save (GameDTORequest gameDTORequest);
    GameDTOResponse update (UUID externalId, GameDTORequest gameDTORequest);
    void delete (UUID externalId);

}

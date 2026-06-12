package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IGameService {

    List<GameDTOResponse> getAllGames(String name,
                                      BigDecimal maxPrice,
                                      BigDecimal minPrice,
                                      LocalDate minReleaseDate,
                                      LocalDate maxReleaseDate,
                                      Boolean statusBlocked,
                                      List<String> genreNames,
                                      String rangeName,
                                      String developerCompany);
    GameDTOResponse getByExternalId (UUID externalId);
    GameDTOResponse save (GameDTORequest gameDTORequest);
    GameDTOResponse update (UUID externalId, GameDTORequest gameDTORequest);
    GameDTOResponse authorized(UUID externalId);
    void desauthorized(UUID externalId);

}

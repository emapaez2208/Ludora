package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.common.exception.GameNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.ageRange.IAgeRangeRepository;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.developer.IDeveloperRepository;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.genre.IGenreRepository;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameService implements IGameService{

    private final IGameRepository gameRepository;
    private final IMapper<GameEntity, GameDTOResponse> responseMapper;
    private final IMapper<GameEntity, GameDTORequest> requestMapper;
    private final IDeveloperRepository developerRepository;
    private final IAgeRangeRepository ageRangeRepository;
    private final IGenreRepository genreRepository;

    @Override
    public List<GameDTOResponse> getAllGames(String name,
                                             BigDecimal maxPrice,
                                             BigDecimal minPrice,
                                             LocalDate minReleaseDate,
                                             LocalDate maxReleaseDate,
                                             Boolean statusBlocked,
                                             List<String> genreNames,
                                             String rangeName,
                                             String developerCompany) {

        PredicateSpecification<GameEntity> spec = PredicateSpecification.allOf(
                GameSpecification.nameContains(name),
                GameSpecification.priceLesserThan(maxPrice),
                GameSpecification.priceGreaterThan(minPrice),
                GameSpecification.releaseDateAfter(minReleaseDate),
                GameSpecification.releaseDateBefore(maxReleaseDate),
                GameSpecification.statusBlockedEquals(statusBlocked),
                GameSpecification.hasGenreNames(genreNames),
                GameSpecification.hasAgeRangeName(rangeName),
                GameSpecification.hasDeveloperName(developerCompany)
        );

        return gameRepository.findAll(spec).stream()
                .distinct()
                .map(responseMapper::toDTO)
                .toList();

    }

    @Override
    public GameDTOResponse getByExternalId(UUID externalId) {
        return gameRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
    }

    @Override
    public GameDTOResponse save(GameDTORequest gameDTORequest) {

        GameEntity entity = requestMapper.toEntity(gameDTORequest);

        DeveloperEntity developer = developerRepository.findByExternalId(gameDTORequest.developerExternalId())
                .orElseThrow(() -> new RuntimeException("Developer no encontrado"));

        entity.setDeveloper(developer);

        AgeRangeEntity ageRange = ageRangeRepository.findByExternalId(gameDTORequest.ageRangeExternalId())
                .orElseThrow(() -> new RuntimeException("Rango de edad no encontrado"));

        entity.setAgeRange(ageRange);

        List<GenreEntity> genres = genreRepository.findAllById(gameDTORequest.genreIds());
        if (genres.isEmpty()) {
            throw new RuntimeException("No se encontraron géneros válidos");
        }
        entity.setGenres(genres);

        GameEntity savedEntity = gameRepository.save(entity);

        return responseMapper.toDTO(savedEntity);
    }

    @Override
    public GameDTOResponse update(UUID externalId, GameDTORequest gameDTORequest) {
        GameEntity existingGame = gameRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        GameEntity updatedData = requestMapper.toEntity(gameDTORequest);

        existingGame.setName(updatedData.getName());
        existingGame.setPrice(updatedData.getPrice());
        existingGame.setReleaseDate(updatedData.getReleaseDate());
        existingGame.setDescription(updatedData.getDescription());
        existingGame.setStatusBlocked(updatedData.getStatusBlocked());

        AgeRangeEntity ageRange = ageRangeRepository.findByExternalId(gameDTORequest.ageRangeExternalId())
                .orElseThrow(() -> new RuntimeException("Rango de edad no encontrado"));
        existingGame.setAgeRange(ageRange);

        List<GenreEntity> genres = genreRepository.findAllById(gameDTORequest.genreIds());
        existingGame.setGenres(genres);

        DeveloperEntity developer = developerRepository.findByExternalId(gameDTORequest.developerExternalId())
                .orElseThrow(() -> new RuntimeException("Developer no encontrado"));
        existingGame.setDeveloper(developer);

        GameEntity savedGame = gameRepository.save(existingGame);

        return responseMapper.toDTO(savedGame);
    }

    @Override
    public void delete(UUID externalId) {
        GameEntity toBeDeleted = gameRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        gameRepository.delete(toBeDeleted);
    }
}

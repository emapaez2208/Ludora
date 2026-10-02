package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.auth.providers.AuthenticatedUserProvider;
import ExperienceGroup.Ludora.features.ageRange.exception.AgeRangeNotFoundException;
import ExperienceGroup.Ludora.features.game.exception.GameIsNotFromTheDevException;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final AuthenticatedUserProvider authenticatedUserProvider;


    /// --------------------------- TRAEMOS TODOS LOS JUEGOS  ( CON FILTROS ) ------------------------------
    @Override
    public Page<GameDTOResponse> getAllGames(int page,
                                             int size,
                                             String name,
                                             BigDecimal maxPrice,
                                             BigDecimal minPrice,
                                             LocalDate minReleaseDate,
                                             LocalDate maxReleaseDate,
                                             List<String> genreNames,
                                             String rangeName,
                                             String developerCompany) {

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The minimum price cannot be negative.");
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The maximum price cannot be negative.");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("The minimum price cannot be greater than the maximum price.");
        }

        if (minReleaseDate != null && maxReleaseDate != null && minReleaseDate.isAfter(maxReleaseDate)) {
            throw new IllegalArgumentException("The start date cannot be later than the end date.");
        }

        LocalDate today = LocalDate.now();

        if (minReleaseDate != null && minReleaseDate.isAfter(today)) {
            throw new IllegalArgumentException("The start date cannot be in the future.");
        }
        if (maxReleaseDate != null && maxReleaseDate.isAfter(today)) {
            throw new IllegalArgumentException("The end date cannot be in the future.");
        }

        PredicateSpecification<GameEntity> spec = PredicateSpecification.allOf(
                GameSpecification.nameContains(name),
                GameSpecification.priceLesserThan(maxPrice),
                GameSpecification.priceGreaterThan(minPrice),
                GameSpecification.releaseDateAfter(minReleaseDate),
                GameSpecification.releaseDateBefore(maxReleaseDate),
                GameSpecification.statusBlockedEquals(false),
                GameSpecification.hasGenreNames(genreNames),
                GameSpecification.hasAgeRangeName(rangeName),
                GameSpecification.hasDeveloperCompany(developerCompany)
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return gameRepository.findAll(Specification.where(spec), pageable)
                .map(responseMapper::toDTO);
    }


    /// -------------------- TRAEMOS UN JUEGO EXTERNAL----------------------

    @Override
    public GameDTOResponse getByExternalId(UUID externalId) {
        return gameRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
    }

    /// -------------------CREACION DE JUEGO PARA DEVELOPERS ---------------------

    @Override
    @PreAuthorize("hasAuthority('CREATE_GAMES')")
    @Transactional
    public GameDTOResponse save(GameDTORequest gameDTORequest) {


        GameEntity entity = requestMapper.toEntity(gameDTORequest);

        DeveloperEntity developer = developerRepository.findByExternalId(authenticatedUserProvider.getCurrentUser().externalId())
                .orElseThrow(() -> new UserNotFoundException("Developer not found"));

        entity.setDeveloper(developer);

        AgeRangeEntity ageRange = ageRangeRepository.findByExternalId(gameDTORequest.ageRangeExternalId())
                .orElseThrow(() -> new AgeRangeNotFoundException("Age range not found"));

        entity.setAgeRange(ageRange);

        List<GenreEntity> genres = new ArrayList<>();

        gameDTORequest.genreNames().forEach(
                genre -> genres.add(genreRepository.findByName(genre).orElseThrow(
                        () -> new EntityNotFoundException("Genre not found")))
        );
        entity.setGenres(genres);

        GameEntity savedEntity = gameRepository.save(entity);

        return responseMapper.toDTO(savedEntity);
    }

    /// -------------------------UPDATE GAME DEVELOPER------------

    @Override
    @PreAuthorize("hasAuthority('UPDATE_GAMES')")
    @Transactional
    public GameDTOResponse update(UUID externalId, GameDTORequest gameDTORequest) {

        GameEntity existingGame = gameRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        // check si el juego corresponde al dev logueado
        if(!existingGame.getDeveloper().getExternalId().equals(authenticatedUserProvider.getCurrentUser().externalId())){
            throw new GameIsNotFromTheDevException();
        }

        existingGame.setName(gameDTORequest.name());
        existingGame.setPrice(gameDTORequest.price());
        existingGame.setReleaseDate(gameDTORequest.releaseDate());
        existingGame.setDescription(gameDTORequest.description());

        AgeRangeEntity ageRange = ageRangeRepository.findByExternalId(gameDTORequest.ageRangeExternalId())
                .orElseThrow(() -> new AgeRangeNotFoundException("Age range not found"));
        existingGame.setAgeRange(ageRange);

        List<GenreEntity> genres = new ArrayList<>();

        gameDTORequest.genreNames().forEach(
                genre -> genres.add(genreRepository.findByName(genre).orElseThrow(
                        () -> new EntityNotFoundException("Genre not found")))
        );
        existingGame.setGenres(genres);

        DeveloperEntity developer = developerRepository.findByExternalId(authenticatedUserProvider.getCurrentUser().externalId())
                .orElseThrow(() -> new UserNotFoundException("Developer not found"));
        existingGame.setDeveloper(developer);

        existingGame.setNeedRevision(true);
        existingGame.setStatusBlocked(true);        /// al modificarlo se pide volver a revisar

        GameEntity savedGame = gameRepository.save(existingGame);

        return responseMapper.toDTO(savedGame);
    }

    /// ------------------AUTHORIZAR JUEGO CON ADMIN------------

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public GameDTOResponse authorized(UUID externalId) {

        GameEntity habilitarGame= gameRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        habilitarGame.setStatusBlocked(false);
        habilitarGame.setNeedRevision(false);
        gameRepository.save(habilitarGame);

        return responseMapper.toDTO(habilitarGame);
    }


    /// ------------------DESAUTHORIZAR JUEGO CON ADMIN------------

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void desauthorized(UUID externalId) {

        GameEntity habilitarGame= gameRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        habilitarGame.setStatusBlocked(true);
        gameRepository.save(habilitarGame);

    }

    /// ---------------- BUSCAR JUEGOS NUEVOS O CON REVISION ----------------

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<GameDTOResponse> getGamesNeedRevision(){
        return gameRepository.findAllByNeedRevision(true)
                .orElseThrow(GameNotFoundException::new)
                .stream().map(responseMapper::toDTO)
                .toList();
    }
    /// -------------- DEVELOPER SOLICITAR REVISION DE UN JUEGO ---------------------

    @Override
    @Transactional
    @PreAuthorize("hasRole('DEVELOPER')")
    public GameDTOResponse askForReviewGame(UUID gameId){

        GameEntity game = gameRepository.findByExternalId(gameId)
                .orElseThrow(GameNotFoundException::new);

        // check si el juego corresponde al dev logueado
        if(!game.getDeveloper().getExternalId().equals(authenticatedUserProvider.getCurrentUser().externalId())){
            throw new GameIsNotFromTheDevException();
        }

        game.setNeedRevision(true);
        GameEntity saved = gameRepository.save(game);
        return responseMapper.toDTO(saved);
    }
}

package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeveloperService implements IDeveloperService {

    private final IDeveloperRepository developerRepository;
    private final IMapper<DeveloperEntity, DeveloperDtoResponse> responseMapper;
    private final IMapper<DeveloperEntity, DeveloperDtoRequest> requestMapper;
    private final IGameResponseMapper gameResponseMapper;

    @Override
    public List<DeveloperDtoResponse> getAllDevelopers(String name,
                                                       String lastName,
                                                       String userName,
                                                       String email,
                                                       Boolean statusBlocked,
                                                       String company) {

        PredicateSpecification<DeveloperEntity> spec = PredicateSpecification.allOf(
                DeveloperSpecification.nameContains(name),
                DeveloperSpecification.lastNameContains(lastName),
                DeveloperSpecification.userNameEquals(userName),
                DeveloperSpecification.emailEquals(email),
                DeveloperSpecification.statusBlockedEquals(statusBlocked),
                DeveloperSpecification.companyContains(company)
        );

        return developerRepository.findAll(spec).stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public DeveloperDtoResponse getByExternalId(UUID externalId) {

        return developerRepository.findByExternalId(externalId).stream()
                .map(responseMapper::toDTO)
                .findFirst()
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found, UserID: " + externalId));
    }

    @Override
    public DeveloperDtoResponse save(DeveloperDtoRequest developerDtoRequest) {


        DeveloperEntity entity = requestMapper.toEntity(developerDtoRequest);


        developerRepository.save(entity);

        return responseMapper.toDTO(entity);
    }

    @Override
    public DeveloperDtoResponse update(UUID externalId,
                                       DeveloperDtoRequest developerDtoRequest) {

        DeveloperEntity entity = developerRepository.findByExternalId(externalId)
                                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        entity.setName(developerDtoRequest.name());
        entity.setLastName(developerDtoRequest.lastName());
        entity.setEmail(developerDtoRequest.email());
        entity.setUserName(developerDtoRequest.userName());
        entity.setPassword(developerDtoRequest.password());
        entity.setCompany(developerDtoRequest.company());

        DeveloperEntity saved =
                developerRepository.save(entity);

        return responseMapper.toDTO(saved);
    }

    @Override
    public List<GameDTOResponse> getGamesByDeveloper(UUID externalId) {

        DeveloperEntity entity = developerRepository.findByExternalId(externalId)
                        .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        return entity.getGames()
                .stream()
                .map(gameResponseMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(UUID externalId) {

        DeveloperEntity toBeDeleted = developerRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        developerRepository.delete(toBeDeleted);
    }
}
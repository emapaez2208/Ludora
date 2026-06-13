package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.credentials.exceptions.CredentialsNotFoundException;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.auth.providers.AuthenticatedUserProvider;
import ExperienceGroup.Ludora.common.exception.PasswordInvalidException;
import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.user.exception.IllegalEmailException;
import ExperienceGroup.Ludora.features.user.exception.UserExistsWithUsernameException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import ExperienceGroup.Ludora.features.user.exception.UserExistsWithEmailException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeveloperService implements IDeveloperService {

    private final IDeveloperRepository developerRepository;
    private final IMapper<DeveloperEntity, DeveloperDtoResponse> responseMapper;
    private final IMapper<DeveloperEntity, DeveloperDtoRequest> requestMapper;
    private final IGameResponseMapper gameResponseMapper;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserProvider authenticatedUser;

    @Override
    @PreAuthorize("hasAuthority('SEE_USERS')")
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
    @PreAuthorize("hasAuthority('SEE_USERS') or #externalId == authentication.principal.externalId")
    public DeveloperDtoResponse getByExternalId(UUID externalId) {

        return developerRepository.findByExternalId(externalId).stream()
                .map(responseMapper::toDTO)
                .findFirst()
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found, UserID: " + externalId));
    }

    @Override
    public DeveloperDtoResponse getMyPerfil(){
        return getByExternalId(authenticatedUser.getCurrentUser().externalId());
    }

    @Override
    @Transactional
    public DeveloperDtoResponse save(DeveloperDtoRequest developerDtoRequest) {

        if(developerRepository.existsByEmail(developerDtoRequest.email())){
            throw new UserExistsWithEmailException("User exists with email register");
        }

        if(developerRepository.existsByUserName(developerDtoRequest.userName())){
            throw new UserExistsWithUsernameException("User exists with username register");
        }

        DeveloperEntity saved = developerRepository.save(requestMapper.toEntity(developerDtoRequest));
        CredentialsEntity credentials = CredentialsEntity.builder()
                .roles(Set.of(roleRepository.findByRole(RolesEnum.ROLE_DEVELOPER)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found"))))
                .enabled(true)
                .accountNonLocked(true)
                .username(developerDtoRequest.userName())
                .externalId(saved.getExternalId())
                .password(passwordEncoder.encode(developerDtoRequest.password().value()))
                .user(saved)
                .build();

        credentialsRepository.save(credentials);

        return responseMapper.toDTO(saved);
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_USERS') or #externalId == authentication.principal.externalId")
    public DeveloperDtoResponse update(UUID externalId,
                                       DeveloperUpdateRequest request) {

        DeveloperEntity entity = developerRepository.findByExternalId(externalId)
                                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        entity.setName(request.name());
        entity.setLastName(request.lastName());
        entity.setCompany(request.company());

        DeveloperEntity saved =
                developerRepository.save(entity);

        return responseMapper.toDTO(saved);
    }

    @Override
    @PreAuthorize("hasAuthority('SEE_USERS') or #externalId == authentication.principal.externalId")
    public List<GameDTOResponse> getGamesByDeveloper(UUID externalId) {

        DeveloperEntity entity = developerRepository.findByExternalId(externalId)
                        .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        return entity.getGames()
                .stream()
                .map(gameResponseMapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_USERS') or #externalId == authentication.principal.externalId")
    public void delete(UUID externalId) {

        DeveloperEntity toBeDeleted = developerRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        CredentialsEntity credentials = searchCredentials(toBeDeleted.getUserName());

        credentials.setEnabled(false);
        credentialsRepository.save(credentials);
    }

    @Override
    @PreAuthorize("hasRole('DEVELOPER')")
    @Transactional
    public void changePassword(ChangePasswordDTO passwordDTO) {
        CredentialsEntity credentials = searchCredentials(authenticatedUser.getCurrentUser().username());

        if(passwordEncoder.matches(passwordDTO.oldPass().value(), credentials.getPassword())){ // verifica si son la misma password
            credentials.setPassword(passwordEncoder.encode(passwordDTO.newPass().value()));
            credentialsRepository.save(credentials);
        }else{
            throw new PasswordInvalidException("The old password is invalid");
        }
    }

    @Override
    @PreAuthorize("hasRole('DEVELOPER')")
    @Transactional
    public void changeEmail(ChangeEmailDTO emailDTO) {

        if(developerRepository.existsByEmail(emailDTO.newEmail())){
            throw new UserExistsWithEmailException("User exists with this email");
        }

        DeveloperEntity developer = developerRepository.findByExternalId(authenticatedUser.getCurrentUser().externalId())
                .orElseThrow(UserNotFoundException::new);

        if(developer.getEmail().value().equals(emailDTO.oldEmail().value())){
            developer.setEmail(emailDTO.newEmail());
            developerRepository.save(developer);
        }else{
            throw new IllegalEmailException("The old email is incorrect");
        }

    }

    private CredentialsEntity searchCredentials(String username){
        return credentialsRepository.findByUsername(username)
                .orElseThrow(() -> new CredentialsNotFoundException("Credentials not found"));
    }
}
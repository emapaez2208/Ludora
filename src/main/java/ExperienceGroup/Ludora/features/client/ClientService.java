package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.credentials.exceptions.CredentialsNotFoundException;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.auth.providers.AuthenticatedUserProvider;
import ExperienceGroup.Ludora.common.exception.PasswordInvalidException;
import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.user.exception.IllegalEmailException;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserExistsWithUsernameException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.ICartService;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientUpdateRequest;
import ExperienceGroup.Ludora.features.user.exception.UserExistsWithEmailException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ClientService implements IClientService{
    private final IClientRepository repository;
    private final IGameRepository gameRepository;
    private final IMapper<ClientEntity,ClientDTOResponse> mapperResponse;
    private final IMapper<ClientEntity,ClientDTORequest> mapperRequest;
    private final IMapper<GameEntity, GameDTOResponse> mapperGamesResponse;
    private final ICartService cartService;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserProvider authenticatedUser;


    @Override
    @PreAuthorize("hasAuthority('SEE_USERS')")
    public List<ClientDTOResponse> getAllClient(String name,
                                                String lastName,
                                                String userName,
                                                String email,
                                                Boolean statusBlocked,
                                                Integer phone,
                                                String street,
                                                Integer numberStreet,
                                                LocalDate birthDate) {

        PredicateSpecification<ClientEntity> spec = PredicateSpecification.allOf(
                ClientSpecification.nameContains(name),
                ClientSpecification.lastNameContains(lastName),
                ClientSpecification.userNameEquals(userName),
                ClientSpecification.emailEquals(email),
                ClientSpecification.statusBlockedEquals(statusBlocked),
                ClientSpecification.phoneEquals(phone),
                ClientSpecification.streetEquals(street),
                ClientSpecification.numberStreetEquals(numberStreet),
                ClientSpecification.birthDateEquals(birthDate)
        );

        return repository.findAll(spec).stream()
                .map(mapperResponse::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('SEE_USERS') or #externalID == authentication.principal.externalId")
    public ClientDTOResponse getByExternalID(UUID externalID) {
        return repository.findByExternalId(externalID).stream()
                .map(mapperResponse::toDTO)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("Client not found"));
    }

    @Override
    @PreAuthorize("hasAuthority('SEE_USERS')")
    public ClientDTOResponse getByUserName(String userName) {

        ClientEntity entity = repository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        return mapperResponse.toDTO(entity);
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public ClientDTOResponse getMyPerfil(){
        return getByExternalID(authenticatedUser.getCurrentUser().externalId());
    }

    @Override
    @Transactional
    public ClientDTOResponse save(ClientDTORequest clientDTORequest) {

        if(repository.existsByEmail(clientDTORequest.email())){
            throw new UserExistsWithEmailException("User exists with this email");
        }

        if(repository.existsByUserName(clientDTORequest.userName())){
            throw new UserExistsWithUsernameException("User exists with this Username");
        }

        ClientEntity saved = mapperRequest.toEntity(clientDTORequest);
        saved.setPoints(0);

        repository.save(saved);

        cartService.crearCarrito(saved.getExternalId());

        CredentialsEntity credentials = CredentialsEntity.builder()
                .roles(Set.of(roleRepository.findByRole(RolesEnum.ROLE_CLIENT).orElseThrow(() -> new EntityNotFoundException("Role not found"))))
                .enabled(true)
                .username(clientDTORequest.userName())
                .externalId(saved.getExternalId())
                .password(passwordEncoder.encode(clientDTORequest.password().value()))
                .user(saved)
                .build();


        credentialsRepository.save(credentials);

        return mapperResponse.toDTO(saved);
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_USERS') or #externalID == authentication.principal.externalId")
    public void delete(UUID externalID) {
        ClientEntity entityDelete = repository.findByExternalId(externalID).
                orElseThrow(()->new UserNotFoundException ("Client not found"));

        CredentialsEntity credentials = searchCredentials(entityDelete.getUserName());

        credentials.setEnabled(false);
        credentialsRepository.save(credentials);
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_USERS') or #id == authentication.principal.externalId")
    public ClientDTOResponse update(UUID id, ClientUpdateRequest dto) {
        ClientEntity clientEntity = repository.findByExternalId(id)
                .orElseThrow(()-> new UserNotFoundException("CLient not found"));

        clientEntity.setName(dto.name());
        clientEntity.setLastName(dto.lastName());
        clientEntity.setPhone(dto.phone());
        clientEntity.setStreet(dto.street());
        clientEntity.setNumberStreet(dto.numberStreet());
        clientEntity.setBirthDate(dto.birthDate());

        repository.save(clientEntity);

        return mapperResponse.toDTO(clientEntity);
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
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
    @PreAuthorize("hasRole('CLIENT')")
    @Transactional
    public void changeEmail(ChangeEmailDTO emailDTO) {

        if(repository.existsByEmail(emailDTO.newEmail())){
            throw new UserExistsWithEmailException("User exists with this email");
        }

        ClientEntity client = repository.findByExternalId(authenticatedUser.getCurrentUser().externalId())
                .orElseThrow(UserNotFoundException::new);

        if(client.getEmail().value().equals(emailDTO.oldEmail().value())){
            client.setEmail(emailDTO.newEmail());
            repository.save(client);
        }else{
            throw new IllegalEmailException("The old email is incorrect");
        }

    // No hace falta PreAuthorize porque busca los juegos del cliente logueado
    public List<GameDTOResponse> getMyGames(){
        return  gameRepository.findAllByClients_ExternalId(
                authenticatedUser.getCurrentUser().externalId()
                ).orElseThrow(() -> new GameNotFoundException("The user not contain games"))
                    .stream()
                    .map(mapperGamesResponse::toDTO)
                    .toList();
    }

    private CredentialsEntity searchCredentials(String username){
        return credentialsRepository.findByUsername(username)
                .orElseThrow(() -> new CredentialsNotFoundException("Credentials to user not found"));
    }
}

package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.ICartService;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
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
    private final IMapper<ClientEntity,ClientDTOResponse> mapperResponse;
    private final IMapper<ClientEntity,ClientDTORequest> mapperRequest;
    private final ICartService cartService;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<ClientDTOResponse> getAllClient(String name,
                                                String lastName,
                                                String email,
                                                Boolean statusBlocked,
                                                Integer phone,
                                                String street,
                                                Integer numberStreet,
                                                LocalDate birthDate) {
        PredicateSpecification<ClientEntity> spec = PredicateSpecification.allOf(
                ClientSpecification.nameContains(name),
                ClientSpecification.lastNameContains(lastName),
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
    public ClientDTOResponse getByExternalID(UUID externalID) {
        return repository.findByExternalId(externalID).stream()
                .map(mapperResponse::toDTO)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("Client not found"));
    }

    @Override
    public ClientDTOResponse getByUserName(String userName) {
        CredentialsEntity credentials = credentialsRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserEntity user = credentials.getUser();

        if(user instanceof ClientEntity){
            return mapperResponse.toDTO((ClientEntity) user);
        }
        throw new UserNotFoundException("El usuario no corresponde a un cliente");
    }

    @Override
    @Transactional
    public ClientDTOResponse save(ClientDTORequest clientDTORequest) {

        ClientEntity saved = repository.save(mapperRequest.toEntity(clientDTORequest));
        cartService.crearCarrito(saved.getExternalId());
        CredentialsEntity credentials = CredentialsEntity.builder()
                .roles(Set.of(roleRepository.findByRole(RolesEnum.ROLE_CLIENT.toString()).orElseThrow(() -> new EntityNotFoundException("Role not found"))))
                .enabled(true)
                .username(clientDTORequest.userName())
                .password(passwordEncoder.encode(clientDTORequest.password().value()))
                .user(saved)  // <- esta bien ??
                .build();

        credentialsRepository.save(credentials);

        return mapperResponse.toDTO(saved);
    }

    @Override
    public void delete(UUID externalID) {
        ClientEntity entityDelete = repository.findByExternalId(externalID).
                orElseThrow(()->new UserNotFoundException ("Client not found"));

        repository.delete(entityDelete);

    }

    @Override
    public ClientDTOResponse update(UUID id, ClientDTORequest dto) {
        ClientEntity clientEntity = repository.findByExternalId(id)
                .orElseThrow(()-> new UserNotFoundException("CLien not found"));

        clientEntity.setName(dto.name());
        clientEntity.setLastName(dto.lastName());
        clientEntity.setEmail(dto.email());  // Habria que sacarlo y crear un endpoint especifico , para username y password igual
        clientEntity.setPhone(dto.phone());
        clientEntity.setStreet(dto.street());
        clientEntity.setNumberStreet(dto.numberStreet());
        clientEntity.setBirthDate(dto.birthDate());

        repository.save(clientEntity);

        return mapperResponse.toDTO(clientEntity);
    }
}

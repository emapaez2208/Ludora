package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.user.UserSpecification;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class ClientService implements IClientService{
    private IClientRepository repository;
    private IMapper<ClientEntity,ClientDTOResponse> mapperResponse;
    private IMapper<ClientEntity,ClientDTORequest> mapperRequest;

    @Override
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
    public ClientDTOResponse getByExternalID(UUID externalID) {
        return repository.findByExternalId(externalID).stream()
                .map(mapperResponse::toDTO)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("Client not found"));
    }

    @Override
    public ClientDTOResponse getByUserName(String userName) {
        return repository.findByUserName(userName).stream()
                .map(mapperResponse::toDTO)
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("Client not found"));
    }

    @Override
    public ClientDTOResponse save(ClientDTORequest clientDTORequest) {

        ClientEntity entity = mapperRequest.toEntity(clientDTORequest);
        repository.save(entity);

        return mapperResponse.toDTO(entity);
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
        clientEntity.setUserName(dto.userName());
        clientEntity.setEmail(dto.email());
        clientEntity.setPassword(dto.password());
        clientEntity.setPhone(dto.phone());
        clientEntity.setStreet(dto.street());
        clientEntity.setNumberStreet(dto.numberStreet());
        clientEntity.setBirthDate(dto.birthDate());

        repository.save(clientEntity);

        return mapperResponse.toDTO(clientEntity);
    }
}

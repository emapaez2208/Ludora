package ExperienceGroup.Ludora.features.client.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.common.utils.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IClientResponseMapper extends IMapper<ClientEntity, ClientDTOResponse> {

    @Override
    @Mapping(source = "email.value", target = "email")
    ClientDTOResponse toDTO(ClientEntity clientEntity);

    @Override
    ClientEntity toEntity(ClientDTOResponse clientDTOResponse);

    default Email map(String email) {
    return email == null ? null : new Email(email);
    }
}
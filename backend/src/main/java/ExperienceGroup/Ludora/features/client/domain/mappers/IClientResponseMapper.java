package ExperienceGroup.Ludora.features.client.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IClientResponseMapper extends IMapper<ClientEntity, ClientDTOResponse> {

    @Override
    ClientDTOResponse toDTO(ClientEntity clientEntity);

    @Override
    ClientEntity toEntity(ClientDTOResponse clientDTOResponse);
}
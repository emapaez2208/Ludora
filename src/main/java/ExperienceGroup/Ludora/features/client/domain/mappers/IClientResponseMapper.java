package ExperienceGroup.Ludora.features.client.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IUserResponseMapper.class})
public interface IClientResponseMapper extends IMapper<ClientEntity, ClientDTOResponse> {

    @Override
    ClientDTOResponse toDTO(ClientEntity clientEntity);

    @Override
    ClientEntity toEntity(ClientDTOResponse clientDTOResponse);
}
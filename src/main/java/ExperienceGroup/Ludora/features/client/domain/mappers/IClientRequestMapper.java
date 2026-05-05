package ExperienceGroup.Ludora.features.client.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserRequestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = {IUserRequestMapper.class})
public interface IClientRequestMapper extends IMapper<ClientEntity,ClientDTORequest> {
    @Override
    ClientDTORequest toDTO(ClientEntity clientEntity) ;

   @Override
   ClientEntity toEntity(ClientDTORequest clientDTORequest) ;
}

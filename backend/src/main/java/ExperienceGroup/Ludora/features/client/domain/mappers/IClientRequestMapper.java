package ExperienceGroup.Ludora.features.client.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IClientRequestMapper extends IMapper<ClientEntity,ClientDTORequest> {
    @Override
    ClientDTORequest toDTO(ClientEntity clientEntity) ;

   @Override
   ClientEntity toEntity(ClientDTORequest clientDTORequest) ;
}

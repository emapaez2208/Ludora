package ExperienceGroup.Ludora.features.user.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper extends IMapper<UserEntity, UserDTORequest> {

    UserEntity toEntity(UserDTORequest userDTORequest);
    UserDTORequest toDTO(UserEntity userEntity);

}

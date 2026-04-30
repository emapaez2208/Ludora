package ExperienceGroup.Ludora.features.user.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserResponseMapper extends IMapper<UserEntity, UserDTOResponse> {

    UserEntity toEntity(UserDTOResponse userDTOResponse);
    UserDTOResponse toDTO(UserEntity userEntity);
}

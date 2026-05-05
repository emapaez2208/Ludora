package ExperienceGroup.Ludora.features.user.domain.mappers;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.common.utils.Password;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper extends IMapper<UserEntity, UserDTORequest> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "statusBlocked", ignore = true)
    @Mapping(target = "games", ignore = true)
    UserEntity toEntity(UserDTORequest userDTORequest);
    UserDTORequest toDTO(UserEntity userEntity);

    default Email mapEmail(String email){
        return email != null ? new Email(email) : null;
    }

    default Password mapPassword(String password){
        return password != null ? new Password(password) : null;
    }
}

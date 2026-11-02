package ExperienceGroup.Ludora.features.admin.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.common.utils.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAdminDTOResponseMapper extends IMapper<AdminEntity, AdminDTOResponse> {

    AdminEntity toEntity(AdminDTOResponse adminDTO);

    @Mapping(source = "email.value", target = "email")
    AdminDTOResponse toDTO(AdminEntity adminEntity);

    default Email map(String email) {
    return email == null ? null : new Email(email);
    }
}

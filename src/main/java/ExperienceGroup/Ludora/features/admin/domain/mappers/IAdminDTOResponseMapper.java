package ExperienceGroup.Ludora.features.admin.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IUserResponseMapper.class})
public interface IAdminDTOResponseMapper extends IMapper<AdminEntity, AdminDTOResponse> {

    AdminEntity toEntity(AdminDTOResponse adminDTO);

    AdminDTOResponse toDTO(AdminEntity adminEntity);
}

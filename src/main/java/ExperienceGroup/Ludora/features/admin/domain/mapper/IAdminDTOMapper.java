package ExperienceGroup.Ludora.features.admin.domain.mapper;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTO;
import ExperienceGroup.Ludora.features.user.domain.mappers.IUserResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IUserResponseMapper.class})
public interface IAdminDTOMapper extends IMapper<AdminEntity, AdminDTO> {

    AdminEntity toEntity(AdminDTO adminDTO);

    AdminDTO toDTO(AdminEntity adminEntity);
}

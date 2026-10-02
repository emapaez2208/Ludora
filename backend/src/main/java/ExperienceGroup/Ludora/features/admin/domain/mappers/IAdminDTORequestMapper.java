package ExperienceGroup.Ludora.features.admin.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAdminDTORequestMapper extends IMapper<AdminEntity, AdminDTORequest> {

    AdminEntity toEntity(AdminDTORequest adminDTORequest);

    AdminDTORequest toDTO(AdminEntity adminEntity);
}

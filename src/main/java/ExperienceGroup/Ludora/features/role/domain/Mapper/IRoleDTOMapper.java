package ExperienceGroup.Ludora.features.role.domain.Mapper;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.role.domain.RoleEntity;
import ExperienceGroup.Ludora.features.role.domain.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface IRoleDTOMapper extends IMapper<RoleEntity, RoleDTO> {
    @Override
    RoleDTO toDTO(RoleEntity roleEntity);

    @Override
    RoleEntity toEntity(RoleDTO roleDTO) ;
}

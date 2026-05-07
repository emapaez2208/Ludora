package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.user.IUserService;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService implements IAdminService{

    private final IAdminRepository adminRepository;
    private final IMapper<AdminEntity, AdminDTOResponse> responseMapper;
    private final IMapper<AdminEntity, AdminDTORequest> requestMapper;
    private final IUserService userService;

    @Override
    public List<AdminDTOResponse> getAllAdmins(UserDTOResponse user, Long employeeId) {
        List<AdminEntity> admins = adminRepository.findAll();

        if(employeeId != null){
            admins.stream()
                    .filter((entity) -> entity.getEmployeeId().equals(employeeId))
                    .toList();
        }

        if(user != null){
            admins.stream()
                    .filter((entity -> entity.getUser().getExternalId().equals(user.externalId())))
                    .toList();
        }

        return admins.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AdminDTOResponse save(AdminDTORequest adminDTO) {
        AdminEntity adminEntity = requestMapper.toEntity(adminDTO);

        userService.save(adminDTO.user());
        adminRepository.save(adminEntity);

        return responseMapper.toDTO(adminEntity);
    }

    @Override
    public void delete(UUID externalId) {

    }
}

package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.user.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService implements IAdminService{

    private final IAdminRepository adminRepository;
    private final IMapper<AdminEntity, AdminDTOResponse> responseMapper;
    private final IMapper<AdminEntity, AdminDTORequest> requestMapper;
    private final IUserService userService;

    @Override
    public List<AdminDTOResponse> getAllAdmins(String name,
                                               String lastName,
                                               String userName,
                                               String role,
                                               String email,
                                               Boolean statusBlocked,
                                               Long employeeId) {
        List<AdminEntity> admins = adminRepository.findAll();

        if(employeeId != null){
            admins.stream()
                    .filter((entity) -> entity.getEmployeeId().equals(employeeId))
                    .toList();
        }

        admins.stream()
                .filter((entity) -> entity.getExternalId().equals(
                        userService.getAllUsers(name, lastName, userName, role, email, statusBlocked)
                )).toList();

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
    @Transactional
    public void delete(Long employeeId) {
        AdminEntity adminEntity = adminRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));

        userService.delete(adminEntity.getExternalId());
        adminRepository.delete(adminEntity);
    }
}

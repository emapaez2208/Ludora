package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService implements IAdminService{

    private final IAdminRepository adminRepository;
    private final IMapper<AdminEntity, AdminDTOResponse> responseMapper;
    private final IMapper<AdminEntity, AdminDTORequest> requestMapper;

    @Override
    public List<AdminDTOResponse> getAllAdmins(String name,
                                               String lastName,
                                               String userName,
                                               String email,
                                               Boolean statusBlocked,
                                               Long employeeId) {

        PredicateSpecification<AdminEntity> spec = PredicateSpecification.allOf(
                AdminSpecification.nameContains(name),
                AdminSpecification.lastNameContains(lastName),
                AdminSpecification.userNameEquals(userName),
                AdminSpecification.emailEquals(email),
                AdminSpecification.statusBlockedEquals(statusBlocked),
                AdminSpecification.employeeIdEquals(employeeId)
        );

        return adminRepository.findAll(spec).stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public AdminDTOResponse getByExternalId(UUID externalId) {
        return adminRepository.findByExternalId(externalId).stream()
                .map(responseMapper::toDTO)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found, UserID: " + externalId));
    }

    @Override
    public AdminDTOResponse save(AdminDTORequest adminDTO) {
        AdminEntity entity = requestMapper.toEntity(adminDTO);

        adminRepository.save(entity);

        return responseMapper.toDTO(entity);
    }

    @Override
    public AdminDTOResponse update(UUID externalId, AdminDTORequest adminDTO) {
        AdminEntity entity = adminRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        entity.setName(adminDTO.name());
        entity.setLastName(adminDTO.lastName());
        entity.setEmail(adminDTO.email());
        entity.setUserName(adminDTO.userName());
        entity.setPassword(adminDTO.password());
        entity.setEmployeeId(adminDTO.employeeId());

        AdminEntity saved = adminRepository.save(entity);

        return responseMapper.toDTO(saved);
    }

    @Override
    public void delete(UUID externalId) {
        AdminEntity toBeDeleted = adminRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        adminRepository.delete(toBeDeleted);
    }
}

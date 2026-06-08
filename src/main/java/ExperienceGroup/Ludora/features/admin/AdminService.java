package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.credentials.exceptions.CredentialsNotFoundException;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminUpdateRequest;
import ExperienceGroup.Ludora.features.user.exception.UserExistsWithEmailException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService{

    private final IAdminRepository adminRepository;
    private final IMapper<AdminEntity, AdminDTOResponse> responseMapper;
    private final IMapper<AdminEntity, AdminDTORequest> requestMapper;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public AdminDTOResponse getByExternalId(UUID externalId) {
        return adminRepository.findByExternalId(externalId).stream()
                .map(responseMapper::toDTO)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found, UserID: " + externalId));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdminDTOResponse save(AdminDTORequest adminDTO) {

        if(adminRepository.existsByEmail(adminDTO.email())){
            throw new UserExistsWithEmailException("User with this email exists");
        }

        AdminEntity entity = adminRepository.save(requestMapper.toEntity(adminDTO));
        CredentialsEntity credentials = CredentialsEntity.builder()
                .roles(Set.of(roleRepository.findByRole(RolesEnum.ROLE_ADMIN).orElseThrow(() -> new EntityNotFoundException("Role not found"))))
                .enabled(true)
                .username(adminDTO.userName())
                .externalId(entity.getExternalId())
                .password(passwordEncoder.encode(adminDTO.password().value()))
                .user(entity)
                .build();

        credentialsRepository.save(credentials);

        return responseMapper.toDTO(entity);
    }

    @Override
    @PreAuthorize("#externalId == authentication.principal.externalId or hasRole('ADMIN')")
    public AdminDTOResponse update(UUID externalId, AdminUpdateRequest adminDTO) {
        AdminEntity entity = adminRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        entity.setName(adminDTO.name());
        entity.setLastName(adminDTO.lastName());
        entity.setEmployeeId(adminDTO.employeeId());

        AdminEntity saved = adminRepository.save(entity);

        return responseMapper.toDTO(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #externalId == authentication.principal.externalId")
    public void delete(UUID externalId) {
        AdminEntity toBeDeleted = adminRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found, userID: " + externalId));

        CredentialsEntity credentials = searchCredentials(toBeDeleted.getUserName());

        credentials.setEnabled(false);
        credentialsRepository.save(credentials);
    }

    private CredentialsEntity searchCredentials(String username){
        return credentialsRepository.findByUsername(username)
                .orElseThrow(() -> new CredentialsNotFoundException("Credentials to user not found"));
    }
}

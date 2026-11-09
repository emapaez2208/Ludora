package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.utils.AdminChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.AdminChangePasswordDTO;
import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAdminService {

    Page<AdminDTOResponse> getAllAdmins(int page,
                                        int size,
                                        String name,
                                        String lastName,
                                        String userName,
                                        String email,
                                        Boolean statusBlocked,
                                        Long employeeId);

    AdminDTOResponse getByExternalId(UUID externalId);

    AdminDTOResponse getMyPerfil();

    AdminDTOResponse save(AdminDTORequest adminDTO);

    AdminDTOResponse update(UUID externalId, AdminUpdateRequest adminDTO);

    void delete(UUID externalId);

    void changePassword(ChangePasswordDTO passwordDTO);

    void changeEmail(ChangeEmailDTO emailDTO);

    void changeUserEmail(UUID externalId, AdminChangeEmailDTO emailDTO);

    void changeUserPassword(UUID externalId, AdminChangePasswordDTO passwordDTO);

    void blockAccount(UUID externalId);

    void unblockAccount(UUID externalId);

    void enableAccount(UUID externalId);
}
package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface IAdminService {

    List<AdminDTOResponse> getAllAdmins(String name,
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
}

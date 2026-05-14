package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;

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

    AdminDTOResponse save(AdminDTORequest adminDTO);

    AdminDTOResponse update(UUID externalId, AdminDTORequest adminDTO);

    void delete(UUID externalId);
}

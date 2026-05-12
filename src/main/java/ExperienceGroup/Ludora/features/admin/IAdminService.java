package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

import java.util.List;

public interface IAdminService {

    List<AdminDTOResponse> getAllAdmins(String name,
                                        String lastName,
                                        String userName,
                                        String role,
                                        String email,
                                        Boolean statusBlocked,
                                        Long employeeId);

    AdminDTOResponse save(AdminDTORequest adminDTO);

    void delete(Long employeeId);
}

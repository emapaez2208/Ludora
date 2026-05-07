package ExperienceGroup.Ludora.features.user;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.role.domain.dto.RoleDTO;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    List<UserDTOResponse> getAllUsers(String name,
                                      String lastName,
                                      String userName,
                                      String role,
                                      String email,
                                      Boolean statusBlocked);

    UserDTOResponse getByExternalId(UUID externalId);

    UserDTOResponse getByUserName(String userName);

    UserDTOResponse save(UserDTORequest userDTORequest);

    void delete(UUID externalId);
}

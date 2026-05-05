package ExperienceGroup.Ludora.features.user;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.role.domain.dto.RoleDTO;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final IMapper<UserEntity, UserDTOResponse> responseMapper;

    @Override
    public List<UserDTOResponse> getAllUsers(String name, String lastName, RoleDTO role, Boolean statusBlocked, List<GameDTOResponse> game) {
        return List.of();
    }

    @Override
    public UserDTOResponse getByExternalId(UUID externalId) {
        return null;
    }

    @Override
    public UserDTOResponse getByUserName(String userName) {
        return null;
    }

    @Override
    public UserDTOResponse save(UserDTORequest userDTORequest) {
        return null;
    }

    @Override
    public void delete(UUID externalId) {

    }
}

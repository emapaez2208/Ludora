package ExperienceGroup.Ludora.features.user;

import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final IMapper<UserEntity, UserDTOResponse> responseMapper;
    private final IMapper<UserEntity, UserDTORequest> requestMapper;

    @Override
    public List<UserDTOResponse> getAllUsers(String name,
                                             String lastName,
                                             String userName,
                                             String role,
                                             String email,
                                             Boolean statusBlocked) {

        PredicateSpecification<UserEntity> spec = PredicateSpecification.allOf(
                UserSpecification.nameContains(name),
                UserSpecification.lastNameContains(lastName),
                UserSpecification.userNameEquals(userName),
                UserSpecification.roleEquals(role),
                UserSpecification.emailEquals(email),
                UserSpecification.statusBlockedEquals(statusBlocked)
        );
        return userRepository.findAll(spec).stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTOResponse getByExternalId(UUID externalId) {
        return userRepository.findByExternalId(externalId).stream()
                .map(responseMapper::toDTO)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserDTOResponse getByUserName(String userName) {
        return userRepository.findByUserName(userName).stream()
                .findFirst()
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserDTOResponse save(UserDTORequest userDTORequest) {

        UserEntity entity = requestMapper.toEntity(userDTORequest);
        userRepository.save(entity);

        return responseMapper.toDTO(entity);
    }

    @Override
    public void delete(UUID externalId) {
        UserEntity toBeDeleted = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(toBeDeleted);
    }
}

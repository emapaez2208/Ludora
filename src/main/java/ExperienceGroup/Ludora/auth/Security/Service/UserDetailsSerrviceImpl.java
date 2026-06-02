package ExperienceGroup.Ludora.auth.Security.Service;

import ExperienceGroup.Ludora.auth.Security.Repsository.CredentialsRepository;
import ExperienceGroup.Ludora.common.exception.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsSerrviceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        return
                credentialsRepository.findByUsername(username).orElseThrow(()->
                        new UserNotFoundException("User not found"));
    }
}

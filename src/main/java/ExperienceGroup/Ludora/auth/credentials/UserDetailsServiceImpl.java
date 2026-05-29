package ExperienceGroup.Ludora.auth.credentials;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CredentialsRepository credentialsRepository;
    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
            throws UsernameNotFoundException {
        return
                credentialsRepository.findByUsername(username).orElseThrow(() -> new
                        UsernameNotFoundException("User not found"));
    }
}
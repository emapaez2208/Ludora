package ExperienceGroup.Ludora.auth;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserDetails authenticate(AuthRequest input) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.username(),
                            input.password()
                    )
            );

        } catch (LockedException e) {

            CredentialsEntity credentials =
                    credentialsRepository.findByUsername(input.username())
                            .orElseThrow(() ->
                                    new BadCredentialsException("Invalid credentials"));

            boolean passwordMatches = passwordEncoder.matches(
                    input.password(),
                    credentials.getPassword()
            );

            if (!passwordMatches) {
                throw new BadCredentialsException("Invalid credentials");
            }

            throw e;
        }

        return credentialsRepository.findByUsername(input.username())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}

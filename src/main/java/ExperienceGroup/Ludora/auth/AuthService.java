package ExperienceGroup.Ludora.auth;

import ExperienceGroup.Ludora.auth.Security.Credential.CredentialsRepository;
import ExperienceGroup.Ludora.auth.DTO.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    public UserDetails authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );
        return
                credentialsRepository.findByUsername(input.username()).orElseThrow(
                        () -> new UsernameNotFoundException("Usuario no encontrado")
                        );
    }
}

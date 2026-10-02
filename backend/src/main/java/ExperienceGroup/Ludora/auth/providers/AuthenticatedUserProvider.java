package ExperienceGroup.Ludora.auth.providers;

import ExperienceGroup.Ludora.auth.dto.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserProvider {

    public AuthUser getCurrentUser(){
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

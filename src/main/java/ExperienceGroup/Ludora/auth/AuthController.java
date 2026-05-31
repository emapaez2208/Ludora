package ExperienceGroup.Ludora.auth;


import ExperienceGroup.Ludora.auth.dto.AuthRequest;
import ExperienceGroup.Ludora.auth.dto.AuthResponse;
import ExperienceGroup.Ludora.auth.jwt.IJwtService;
import ExperienceGroup.Ludora.features.client.IClientService;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.developer.IDeveloperService;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final IClientService clientService;
    private final IDeveloperService developerService;
    private final IJwtService jwtService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @PostMapping("/clients/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTOResponse registerClient(@Valid @RequestBody ClientDTORequest clientRequest){
        return clientService.save(clientRequest);
    }

    @PostMapping("/developers/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperDtoResponse registerDeveloper(@Valid @RequestBody DeveloperDtoRequest developerRequest) {
        return developerService.save(developerRequest);
    }
}

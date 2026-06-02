package ExperienceGroup.Ludora.auth;

import ExperienceGroup.Ludora.auth.dto.AuthRequest;
import ExperienceGroup.Ludora.auth.dto.AuthResponse;
import ExperienceGroup.Ludora.auth.jwt.JwtService;
import ExperienceGroup.Ludora.features.client.IClientService;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.developer.IDeveloperService;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final IClientService clientService;
    private final IDeveloperService developerService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/register/client")
    public ResponseEntity<ClientDTOResponse> registerClient(@RequestBody ClientDTORequest clientDTORequest){
        return new ResponseEntity<>(clientService.save(clientDTORequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/register/developer")
    public ResponseEntity<DeveloperDtoResponse> registerDeveloper(@RequestBody DeveloperDtoRequest developerDtoRequest){
        return new ResponseEntity<>(developerService.save(developerDtoRequest),
                HttpStatus.CREATED);
    }
}

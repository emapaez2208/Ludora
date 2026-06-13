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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user sign-in and registration across different roles")
public class AuthController {

    private final AuthService authService;
    private final IClientService clientService;
    private final IDeveloperService developerService;
    private final IJwtService jwtService;

    @Operation(
            summary = "User Login",
            description = "Authenticates user credentials (email/username and password) and returns a valid JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful. Returns the JWT token.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Invalid credentials provided.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. Invalid request body structure.",
                    content = @Content
            )
    })

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Operation(
            summary = "Registrar un nuevo Cliente",
            description = "Crea una cuenta de tipo Cliente en la plataforma validando los datos enviados en el cuerpo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente registrado con éxito.",
                    content = @Content(schema = @Schema(implementation = ClientDTOResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de registro inválidos (falla en las validaciones de @Valid).",
                    content = @Content
            )
    })

    @PostMapping("/register/client")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTOResponse registerClient(@Valid @RequestBody ClientDTORequest clientRequest){
        return clientService.save(clientRequest);
    }
    @Operation(
            summary = "Register a new Developer",
            description = "Creates a new Developer account in the platform after validating the provided request data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Developer registered successfully.",
                    content = @Content(schema = @Schema(implementation = DeveloperDtoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. Validation constraints failed.",
                    content = @Content
            )
    })

    @PostMapping("/register/developer")
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperDtoResponse registerDeveloper(@Valid @RequestBody DeveloperDtoRequest developerRequest) {
        return developerService.save(developerRequest);
    }
}

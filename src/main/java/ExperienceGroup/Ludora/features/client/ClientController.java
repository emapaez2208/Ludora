package ExperienceGroup.Ludora.features.client;


import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
@Tag(name = "Clients", description = "Endpoints for client account management, profile actions, credentials updates, and game libraries")
public class ClientController {

    private final IClientService clientService;

    @Operation(
            summary = "List all clients",
            description = "Retrieves a filtered list of clients based on multiple optional query parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients list retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to access this resource.", content = @Content)
    })

    @GetMapping
    ResponseEntity<Page<ClientDTOResponse>> getAll(@Parameter(description = "Page number of the list") @RequestParam int page,
                                                   @Parameter(description = "Page size of the list") @RequestParam int size,
                                                   @Parameter(description = "Filter by first name") @RequestParam(required = false) String name,
                                                   @Parameter(description = "Filter by last name") @RequestParam(required = false) String lastName,
                                                   @Parameter(description = "Filter by username") @RequestParam(required = false) String userName,
                                                   @Parameter(description = "Filter by email address") @RequestParam(required = false) String email,
                                                   @Parameter(description = "Filter by blocked status (true/false)") @RequestParam(required = false) Boolean statusBlocked,
                                                   @Parameter(description = "Filter by phone number") @RequestParam(required = false) Integer phone,
                                                   @Parameter(description = "Filter by street name") @RequestParam(required = false) String street,
                                                   @Parameter(description = "Filter by street number") @RequestParam(required = false) Integer numberStreet,
                                                   @Parameter(description = "Filter by birth date (Format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate birthDate
    ) {
        return ResponseEntity.ok(clientService.getAllClient(
                page,
                size,
                name,
                lastName,
                userName,
                email,
                statusBlocked,
                phone,
                street,
                numberStreet,
                birthDate)
        );
    }

    @Operation(
            summary = "Get current client profile",
            description = "Retrieves account details of the currently authenticated client user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client profile details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Client account is blocked, disabled, or expired.", content = @Content)
    })

    @GetMapping("/profile")
    ResponseEntity<ClientDTOResponse> getMyPerfil(){
        return ResponseEntity.ok(clientService.getMyPerfil());
    }

    @Operation(
            summary = "Get my game library",
            description = "Retrieves the list of games purchased or owned by the currently authenticated client user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game library retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Client account is blocked, disabled, or expired.", content = @Content)
    })
    @GetMapping("/library")
    ResponseEntity<List<GameDTOResponse>> getMyGames(){
        return ResponseEntity.ok(clientService.getMyGames());
    }

    @Operation(
            summary = "Get client by ID",
            description = "Fetches the data logs of a specific client using their external unique identifier (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client profile found successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to access this client.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No client found with the given UUID specification.", content = @Content)
    })
   @GetMapping("/{id}")
    ResponseEntity<ClientDTOResponse> getById (@Parameter(description = "Unique UUID of the client", required = true) @PathVariable UUID id ){
        return ResponseEntity.ok(clientService.getByExternalID(id));
    }

    @Operation(
            summary = "Register a client account",
            description = "Directly creates a new client account profile in the system database after validating inputs."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client account created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload structure or validation constraints failed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to create client accounts.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. Email or username already exists.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity. Invalid email format or password does not satisfy security requirements.", content = @Content)
    })

    @PostMapping
    ResponseEntity<ClientDTOResponse> save (@Valid @RequestBody ClientDTORequest clientDTORequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientDTORequest));
    }

    @Operation(
            summary = "Update client profile details",
            description = "Modifies personal account details of an existing client profile identified by its UUID status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client account updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid profile update constraints provided.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to update this client.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client target not found.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity. Invalid email format or password does not satisfy security requirements.", content = @Content)
    })

    @PutMapping("/{id}")
    ResponseEntity<ClientDTOResponse> update (@Parameter(description = "UUID of the client to update", required = true) @PathVariable UUID id,
                                              @Valid @RequestBody ClientUpdateRequest dtoRequest){
        return ResponseEntity.ok(clientService.update(id,dtoRequest));
    }

    @Operation(
            summary = "Delete client account",
            description = "Deactivates or deletes a client account from the system registry using its unique UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client account deleted successfully (No Content)."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to delete this client.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client target not found.", content = @Content)
    })

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete (@Parameter(description = "UUID of the client to delete", required = true) @PathVariable UUID id){
        clientService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Change security password",
            description = "Allows the currently authenticated client to update their account security access password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully."),
            @ApiResponse(responseCode = "400", description = "Validation constraints failed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Current password is incorrect.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Client account is blocked, disabled, or expired.", content = @Content)
    })
    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        clientService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change contact email address",
            description = "Allows the currently authenticated client to update their account contact email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email address updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload structure or validation constraints failed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Client account is blocked, disabled, or expired.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. Email address already exists.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity. Invalid email format.", content = @Content)
    })

    @PutMapping("/profile/changeEmail")
    ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailDTO emailDTO){
        clientService.changeEmail(emailDTO);
        return ResponseEntity.ok().build();
    }
}
package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoRequest;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperDtoResponse;
import ExperienceGroup.Ludora.features.developer.domain.dto.DeveloperUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@AllArgsConstructor
@RequestMapping("/developers")
@Tag(name = "Developers", description = "Endpoints for developer/studio account management, profile actions, credentials updates, and published games catalogs")
public class DeveloperController {

    private final IDeveloperService developerService;

    @Operation(
            summary = "List all developers",
            description = "Retrieves a filtered list of developers or studios based on multiple optional query parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developers list retrieved successfully.")
    })

    @GetMapping
    ResponseEntity<List<DeveloperDtoResponse>> getAll(
            @Parameter(description = "Filter by representative first name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by representative last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter by username") @RequestParam(required = false) String userName,
            @Parameter(description = "Filter by corporate email address") @RequestParam(required = false) String email,
            @Parameter(description = "Filter by blocked status (true/false)") @RequestParam(required = false) Boolean statusBlocked,
            @Parameter(description = "Filter by company or studio name") @RequestParam(required = false) String company
    ){

        return ResponseEntity.ok(developerService.getAllDevelopers(
                        name,
                        lastName,
                        userName,
                        email,
                        statusBlocked,
                        company
                )
        );
    }

    @Operation(
            summary = "Get developer by ID",
            description = "Fetches the data logs of a specific developer profile using their external unique identifier (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer profile found successfully."),
            @ApiResponse(responseCode = "404", description = "No developer found with the given UUID specification.", content = @Content)
    })

    @GetMapping("/{id}")
    ResponseEntity<DeveloperDtoResponse> getById(
            @Parameter(description = "Unique UUID of the developer", required = true) @PathVariable UUID id
    ){

        return ResponseEntity.ok(
                developerService.getByExternalId(id)
        );
    }
    @Operation(
            summary = "Get current developer profile",
            description = "Retrieves account details of the currently authenticated developer user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer profile details retrieved successfully.")
    })

    @GetMapping("/perfil")
    ResponseEntity<DeveloperDtoResponse> getMyPerfil(){
        return ResponseEntity.ok(developerService.getMyPerfil());
    }


    @Operation(
            summary = "Register a developer account",
            description = "Directly creates a new developer or studio account profile in the system after validating company data inputs."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer account created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload structure or validation constraints failed.", content = @Content)
    })
    @PostMapping
    ResponseEntity<DeveloperDtoResponse> create(@Valid @RequestBody DeveloperDtoRequest developerDtoRequest){

        return ResponseEntity.ok(
                developerService.save(developerDtoRequest)
        );
    }

    @Operation(
            summary = "Update developer details",
            description = "Modifies profile or company details of an existing developer account identified by its UUID status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer account updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid profile update constraints provided.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Developer target not found.", content = @Content)
    })

    @PutMapping("/{id}")
    ResponseEntity<DeveloperDtoResponse> update(@Parameter(description = "UUID of the developer to update", required = true) @PathVariable UUID id,
                                                @Valid @RequestBody DeveloperUpdateRequest developerDtoRequest
    ){

        return ResponseEntity.ok(
                developerService.update(id, developerDtoRequest)
        );
    }

    @Operation(
            summary = "Get games by developer",
            description = "Retrieves the list of video games published or owned by the studio corresponding to the provided ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer games catalog retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Developer target not found.", content = @Content)
    })

    @GetMapping("/{id}/games")
    ResponseEntity<List<GameDTOResponse>> getGamesByDeveloper(@Parameter(description = "UUID of the developer to fetch games from", required = true) @PathVariable UUID id){
        return ResponseEntity.ok(
                developerService.getGamesByDeveloper(id)
        );
    }

    @Operation(
            summary = "Delete developer account",
            description = "Deactivates or deletes a developer studio account from the system registry using its unique UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Developer account deleted successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "Developer target not found.", content = @Content)
    })

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "UUID of the developer to delete", required = true) @PathVariable UUID id){
        developerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Change security password",
            description = "Allows the currently authenticated developer to update their corporate account access password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully."),
            @ApiResponse(responseCode = "400", description = "Current password mismatch or invalid complexity constraints.", content = @Content)
    })

    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        developerService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change contact email address",
            description = "Allows the currently authenticated developer to update their studio contact email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email address updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid data format or email address already registered by another account.", content = @Content)
    })

    @PutMapping("/profile/changeEmail")
    ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailDTO emailDTO){
        developerService.changeEmail(emailDTO);
        return ResponseEntity.ok().build();
    }
}

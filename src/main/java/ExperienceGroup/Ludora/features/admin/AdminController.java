package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/admins")
@Tag(name = "Admins", description = "Endpoints for administrator management, profile operations, and account moderation actions")
public class AdminController {

    ///  ------------------------------------Listar ADMINS  -----------

    private final IAdminService adminService;

    @Operation(
            summary = "List administrators",
            description = "Retrieves a filtered list of administrators based on the provided search query parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrators list retrieved successfully.")
    })
    @GetMapping
    ResponseEntity<List<AdminDTOResponse>> getAll(@Parameter(description = "Filter by first name") @RequestParam(required = false) String name,
                                                  @Parameter(description = "Filter by last name") @RequestParam(required = false) String lastName,
                                                  @Parameter(description = "Filter by username") @RequestParam(required = false) String userName,
                                                  @Parameter(description = "Filter by email address") @RequestParam(required = false) String email,
                                                  @Parameter(description = "Filter by blocked status (true/false)") @RequestParam(required = false) Boolean statusBlocked,
                                                  @Parameter(description = "Filter by employee ID") @RequestParam(required = false) Long employeeId){
        return ResponseEntity.ok(adminService.getAllAdmins(name, lastName, userName, email, statusBlocked, employeeId));
    }

    ///  -------------------------------ADMINS POR ID(Perfiles) -------------------

    @Operation(
            summary = "Get administrator by ID",
            description = "Fetches the profile details of a specific administrator using their external unique identifier (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrator found successfully."),
            @ApiResponse(responseCode = "404", description = "No administrator found with the given ID.", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<AdminDTOResponse> getById(@Parameter(description = "Unique UUID of the administrator", required = true) @PathVariable UUID id){
        return ResponseEntity.ok(adminService.getByExternalId(id));
    }

    ///  ------------------------------- Traerse a si mismo(se perfil) -----------------------

    @Operation(
            summary = "Get current admin profile",
            description = "Retrieves detailed information of the currently authenticated administrator."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile details retrieved successfully.")
    })
    @GetMapping("/profile")
    ResponseEntity<AdminDTOResponse> getMyPerfil(){
        return ResponseEntity.ok(adminService.getMyPerfil());
    }

    /// --------------------------------CREAR CUENTA ADMIN
    @Operation(
            summary = "Create administrator account",
            description = "Registers a new administrator account in the system after validating the request payload."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrator account created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request payload structure or failed validations.", content = @Content)
    })
    @PostMapping
    ResponseEntity<AdminDTOResponse> create(@Valid @RequestBody AdminDTORequest adminDTORequest){
        return ResponseEntity.ok(adminService.save(adminDTORequest));
    }

    /// --------------------------------------- MODIFICAR ADMIN ------------------------------

    @Operation(
            summary = "Update administrator",
            description = "Updates the data of an existing administrator account identified by its UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrator updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid update data constraints.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Administrator not found.", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<AdminDTOResponse> update(@Parameter(description = "UUID of the administrator to update", required = true) @PathVariable UUID id,
                                            @Valid @RequestBody AdminUpdateRequest adminDTORequest){
        return ResponseEntity.ok(adminService.update(id, adminDTORequest));
    }

    /// -----------------------------------BAJA LOGICA DE CUENTA -----------------------

    @Operation(
            summary = "Soft delete account",
            description = "Logically deactivates an administrator's account by its UUID without deleting rows from the physical database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deactivated successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "Administrator not found.", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "UUID of the administrator to deactivate", required = true) @PathVariable UUID id){
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /// -----------------------------------CAMBIO DE PASSWORD --------------------------------

    @Operation(
            summary = "Change password",
            description = "Allows the currently authenticated administrator to update their security password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully."),
            @ApiResponse(responseCode = "400", description = "Current password mismatch or invalid formatting criteria.", content = @Content)
    })
    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        adminService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    /// -----------------------------------CAMBIO DE PASSWORD ------------------------

    @Operation(
            summary = "Change email address",
            description = "Allows the currently authenticated administrator to update their contact email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email address updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload or email address already in use.", content = @Content)
    })
    @PutMapping("/profile/changeEmail")
    ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailDTO emailDTO){
        adminService.changeEmail(emailDTO);
        return ResponseEntity.ok().build();
    }

    /// ----------------------------------BANEAR UN USUARIO O DEVELOPER

    @Operation(
            summary = "Ban/Block account",
            description = "Restricts a User or Developer account from accessing the platform dynamically."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account blocked successfully."),
            @ApiResponse(responseCode = "404", description = "Target account not found.", content = @Content)
    })
    @PatchMapping("/{id}/block")
    ResponseEntity<Void> blockAccount(@Parameter(description = "UUID of the target account to block", required = true) @PathVariable UUID id) {
        adminService.blockAccount(id);
        return ResponseEntity.noContent().build();
    }

    /// ----------------------------------DESBANEAR UN USUARIO O DEVELOPER

    @Operation(
            summary = "Unban/Unblock account",
            description = "Removes access restrictions for a previously blocked User or Developer account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account unblocked successfully."),
            @ApiResponse(responseCode = "404", description = "Target account not found.", content = @Content)
    })
    @PatchMapping("/{id}/unblock")
    ResponseEntity<Void> unblockAccount(@Parameter(description = "UUID of the target account to unblock", required = true) @PathVariable UUID id) {
        adminService.unblockAccount(id);
        return ResponseEntity.noContent().build();
    }

    /// -------------------------------ALTA LOGICA -------------------///

    @Operation(
            summary = "Soft delete recovery (Enable account)",
            description = "Re-enables an administrator account that was previously logically deactivated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account re-enabled successfully."),
            @ApiResponse(responseCode = "404", description = "Administrator not found.", content = @Content)
    })
    @PatchMapping("/{id}/enable")
    ResponseEntity<Void> enableAccount(@Parameter(description = "UUID of the administrator to re-enable", required = true) @PathVariable UUID id) {
        adminService.enableAccount(id);
        return ResponseEntity.noContent().build();
    }

}

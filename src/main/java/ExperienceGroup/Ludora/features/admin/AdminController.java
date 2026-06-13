package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.utils.ChangeEmailDTO;
import ExperienceGroup.Ludora.common.utils.ChangePasswordDTO;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTORequest;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import ExperienceGroup.Ludora.features.admin.domain.dto.AdminUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    ///  ------------------------------------Listar ADMINS  -----------

    private final IAdminService adminService;
    @GetMapping
    ResponseEntity<List<AdminDTOResponse>> getAll(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String userName,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) Boolean statusBlocked,
                                                  @RequestParam(required = false) Long employeeId){

        return ResponseEntity.ok(adminService.getAllAdmins(name, lastName, userName, email, statusBlocked, employeeId));
    }

    ///  -------------------------------ADMINS POR ID(Perfiles) -------------------

    @GetMapping("/{id}")
    ResponseEntity<AdminDTOResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(adminService.getByExternalId(id));
    }

    ///  ------------------------------- Traerse a si mismo(se perfil) -----------------------

    @GetMapping("/profile")
    ResponseEntity<AdminDTOResponse> getMyPerfil(){
        return ResponseEntity.ok(adminService.getMyPerfil());
    }

    /// --------------------------------CREAR CUENTA ADMIN
    ///
    @PostMapping
    ResponseEntity<AdminDTOResponse> create(@Valid @RequestBody AdminDTORequest adminDTORequest){
        return ResponseEntity.ok(adminService.save(adminDTORequest));
    }

    /// --------------------------------------- MODIFICAR ADMIN ------------------------------

    @PutMapping("/{id}")
    ResponseEntity<AdminDTOResponse> update(@PathVariable UUID id,
                                            @Valid @RequestBody AdminUpdateRequest adminDTORequest){

        return ResponseEntity.ok(adminService.update(id, adminDTORequest));
    }

    /// -----------------------------------BAJA LOGICA DE CUENTA -----------------------

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id){
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /// -----------------------------------CAMBIO DE PASSWORD --------------------------------

    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        adminService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    /// -----------------------------------CAMBIO DE PASSWORD ------------------------

    @PutMapping("/profile/changeEmail")
    ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailDTO emailDTO){
        adminService.changeEmail(emailDTO);
        return ResponseEntity.ok().build();
    }

    /// ----------------------------------BANEAR UN USUARIO O DEVELOPER

    @PatchMapping("/{id}/block")
    ResponseEntity<Void> blockAccount(@PathVariable UUID id) {
        adminService.blockAccount(id);
        return ResponseEntity.noContent().build();
    }

    /// ----------------------------------DESBANEAR UN USUARIO O DEVELOPER

    @PatchMapping("/{id}/unblock")
    ResponseEntity<Void> unblockAccount(@PathVariable UUID id) {
        adminService.unblockAccount(id);
        return ResponseEntity.noContent().build();
    }

    /// -------------------------------ALTA LOGICA -------------------///

    @PatchMapping("/{id}/enable")
    ResponseEntity<Void> enableAccount(@PathVariable UUID id) {
        adminService.enableAccount(id);
        return ResponseEntity.noContent().build();
    }

}

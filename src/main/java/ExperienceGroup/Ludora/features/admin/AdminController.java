package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.common.exception.dto.ChangePasswordDTO;
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

    @GetMapping("/{id}")
    ResponseEntity<AdminDTOResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(adminService.getByExternalId(id));
    }

    @GetMapping("/profile")
    ResponseEntity<AdminDTOResponse> getMyPerfil(){
        return ResponseEntity.ok(adminService.getMyPerfil());
    }

    @PostMapping
    ResponseEntity<AdminDTOResponse> create(@Valid @RequestBody AdminDTORequest adminDTORequest){
        return ResponseEntity.ok(adminService.save(adminDTORequest));
    }

    @PutMapping("/{id}")
    ResponseEntity<AdminDTOResponse> update(@PathVariable UUID id,
                                            @Valid @RequestBody AdminUpdateRequest adminDTORequest){

        return ResponseEntity.ok(adminService.update(id, adminDTORequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id){
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        adminService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }
    
}

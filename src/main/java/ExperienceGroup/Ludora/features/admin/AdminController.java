package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.dto.AdminDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    
}

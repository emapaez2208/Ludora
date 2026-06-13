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

@RestController
@AllArgsConstructor
@RequestMapping("/developers")
public class DeveloperController {

    private final IDeveloperService developerService;

    @GetMapping
    ResponseEntity<List<DeveloperDtoResponse>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean statusBlocked,
            @RequestParam(required = false) String company
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

    @GetMapping("/{id}")
    ResponseEntity<DeveloperDtoResponse> getById(
            @PathVariable UUID id
    ){

        return ResponseEntity.ok(
                developerService.getByExternalId(id)
        );
    }

    @GetMapping("/perfil")
    ResponseEntity<DeveloperDtoResponse> getMyPerfil(){
        return ResponseEntity.ok(developerService.getMyPerfil());
    }

    @PostMapping
    ResponseEntity<DeveloperDtoResponse> create(@Valid @RequestBody DeveloperDtoRequest developerDtoRequest){

        return ResponseEntity.ok(
                developerService.save(developerDtoRequest)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<DeveloperDtoResponse> update(@PathVariable UUID id,@Valid @RequestBody DeveloperUpdateRequest developerDtoRequest){

        return ResponseEntity.ok(
                developerService.update(id, developerDtoRequest)
        );
    }

    @GetMapping("/{id}/games")
    ResponseEntity<List<GameDTOResponse>> getGamesByDeveloper(@PathVariable UUID id){

        return ResponseEntity.ok(
                developerService.getGamesByDeveloper(id)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id){
        developerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile/changePassword")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO){
        developerService.changePassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile/changeEmail")
    ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailDTO emailDTO){
        developerService.changeEmail(emailDTO);
        return ResponseEntity.ok().build();
    }
}

package ExperienceGroup.Ludora.features.client;


import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientUpdateRequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @GetMapping
    ResponseEntity<List<ClientDTOResponse>> getAll(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String lastName,
                                                         @RequestParam(required = false) String userName,
                                                         @RequestParam(required = false) String email,
                                                         @RequestParam(required = false) Boolean statusBlocked,
                                                         @RequestParam(required = false) Integer phone,
                                                         @RequestParam(required = false) String street,
                                                         @RequestParam(required = false) Integer numberStreet,
                                                         @RequestParam(required = false) LocalDate birthDate
    ) {
        return ResponseEntity.ok(clientService.getAllClient(
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

    @GetMapping("/perfil")
    ResponseEntity<ClientDTOResponse> getMyPerfil(){
        return ResponseEntity.ok(clientService.getMyPerfil());
    }

    @GetMapping("/myGames")
    ResponseEntity<List<GameDTOResponse>> getMyGames(){
        return ResponseEntity.ok(clientService.getMyGames());
    }

   @GetMapping("/{id}")
    ResponseEntity<ClientDTOResponse> getById (@PathVariable UUID id ){
        return ResponseEntity.ok(clientService.getByExternalID(id));
    }

    @PostMapping
    ResponseEntity<ClientDTOResponse> save (@Valid @RequestBody ClientDTORequest clientDTORequest){
        return ResponseEntity.ok(clientService.save(clientDTORequest));
    }

    @PutMapping("/{id}")
    ResponseEntity<ClientDTOResponse> update (@PathVariable UUID id , @Valid @RequestBody ClientUpdateRequest dtoRequest){
        return ResponseEntity.ok(clientService.update(id,dtoRequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete (@PathVariable UUID id){
        clientService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
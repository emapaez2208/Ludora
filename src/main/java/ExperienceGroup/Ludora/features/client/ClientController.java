package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTORequest;
import ExperienceGroup.Ludora.features.client.domain.dto.ClientDTOResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.RequestHandledEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/Client")
public class ClientController {

    private IClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTOResponse>> getAll(@RequestParam(required = false) String name,
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

   @GetMapping("/{id}")
    public ResponseEntity<ClientDTOResponse> getById (@PathVariable UUID id ){
        return ResponseEntity.ok(clientService.getByExternalID(id));
    }

    @PostMapping
    public ResponseEntity<ClientDTOResponse> save (@Valid @RequestBody ClientDTORequest clientDTORequest){
        return ResponseEntity.ok(clientService.save(clientDTORequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTOResponse> update (@PathVariable UUID id , @Valid @RequestBody ClientDTORequest dtoRequest){
        return ResponseEntity.ok(clientService.update(id,dtoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable UUID id){
        clientService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
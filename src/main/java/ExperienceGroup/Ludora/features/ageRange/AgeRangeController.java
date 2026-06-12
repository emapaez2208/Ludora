package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTORequest;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/ageRanges")
public class AgeRangeController {
    private final IAgeRangeService ageRangeService;

    @GetMapping
    ResponseEntity<List<AgeRangeDTOResponse>> getAll(){
        return ResponseEntity.ok(ageRangeService.getAllAgeRange());
    }

    @GetMapping("/{externalId}")
    ResponseEntity<AgeRangeDTOResponse> getByExternalId (@PathVariable UUID externalId){
        return ResponseEntity.ok(ageRangeService.getByExternalId(externalId));
    }

    @PostMapping
    ResponseEntity<AgeRangeDTOResponse> create (@Valid @RequestBody AgeRangeDTORequest ageRangeDTORequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(ageRangeService.save(ageRangeDTORequest));
    }

    @PutMapping("/{externalId}")
    ResponseEntity<AgeRangeDTOResponse> update(
            @PathVariable UUID externalId,
            @Valid @RequestBody AgeRangeDTORequest ageRangeDTORequest
    ){
        return ResponseEntity.ok(ageRangeService.update(externalId, ageRangeDTORequest));
    }

    @DeleteMapping("/{externalId}")
    ResponseEntity<Void> delete(@PathVariable UUID externalId){
        ageRangeService.delete(externalId);
        return ResponseEntity.noContent().build();
    }

}

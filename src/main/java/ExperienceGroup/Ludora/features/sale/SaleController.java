package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SaleController {

    private final ISaleService saleService;

    @PostMapping
    public ResponseEntity<SaleDTOResponse> create(@Valid @RequestBody SaleDTORequest saleDTORequest) {
        SaleDTOResponse response = saleService.create(saleDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<SaleDTOResponse> getByExternalId(@PathVariable UUID externalId) {
        return ResponseEntity.ok(saleService.getByExternalId(externalId));
    }

    @GetMapping("/client/{clientExternalId}")
    public ResponseEntity<List<SaleDTOResponse>> getSalesByClient(@PathVariable UUID clientExternalId) {
        return ResponseEntity.ok(saleService.getSalesByClient(clientExternalId));
    }
}
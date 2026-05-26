package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @GetMapping
    public ResponseEntity<List<SaleDTOResponse>> getAllSales(
            @RequestParam(required = false) UUID externalId,
            @RequestParam(required = false) LocalDateTime minDate,
            @RequestParam(required = false) LocalDateTime maxDate,
            @RequestParam(required = false) ESaleStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<UUID> clientIds,
            @RequestParam(required = false) List<UUID> gameIds) {

        return ResponseEntity.ok(saleService.getAllSales(externalId, minDate, maxDate, status, minPrice, maxPrice, clientIds, gameIds));
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
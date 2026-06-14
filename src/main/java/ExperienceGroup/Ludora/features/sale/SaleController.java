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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
@Tag(name = "Sales", description = "Endpoints for handling purchase orders, transaction tracking, and payment gateway integrations")
public class SaleController {

    private final ISaleService saleService;

    @Operation(
            summary = "Initialize a sale checkout order",
            description = "Registers a new purchase or sale order record inside the system with the targeted games dataset, leaving its process state pending."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale checkout order initialized successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or verification conditions failed.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SaleDTOResponse> create(@Valid @RequestBody SaleDTORequest saleDTORequest) {
        SaleDTOResponse response = saleService.create(saleDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Process sale invoice payment (Mercado Pago)",
            description = "Generates the dynamic payment links or processes the checkout request structure via Mercado Pago. Returns the redirection URL string or token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment gateway preference context loaded successfully."),
            @ApiResponse(responseCode = "404", description = "No sale instance found matching the given UUID description.", content = @Content)
    })

    @PostMapping("/pay/{externalId}")
    public ResponseEntity<String> paySale(@Parameter(description = "The target sale unique external UUID to settle", required = true) @PathVariable UUID externalId){
        return ResponseEntity.ok(saleService.paySaleMP(externalId));
    }

    @Operation(
            summary = "List sales logs with advanced filters",
            description = "Retrieves an explicit transaction log index from the database system registry, supporting dynamic fields like price caps, status types, or multiple user lists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered historical transactions list retrieved successfully.")
    })

    @GetMapping
    public ResponseEntity<List<SaleDTOResponse>> getAllSales(
            @Parameter(description = "Target specific transaction UUID") @RequestParam(required = false) UUID externalId,
            @Parameter(description = "Initial timestamp scope boundary (ISO format)") @RequestParam(required = false) LocalDateTime minDate,
            @Parameter(description = "Final timestamp scope boundary (ISO format)") @RequestParam(required = false) LocalDateTime maxDate,
            @Parameter(description = "Current stage transaction tag state (PENDING, PAID, CANCELED)") @RequestParam(required = false) ESaleStatus status,
            @Parameter(description = "Minimum gross total cost boundary") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum gross total cost boundary") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Collection array of specific clients UUIDs") @RequestParam(required = false) List<UUID> clientIds,
            @Parameter(description = "Collection array of digital games UUID components") @RequestParam(required = false) List<UUID> gameIds) {    return ResponseEntity.ok(saleService.getAllSales(externalId, minDate, maxDate, status, minPrice, maxPrice, clientIds, gameIds));
    }

    @Operation(
            summary = "Get transaction details by external ID",
            description = "Fetches the full structure logs from a specific transaction context matching its unique reference UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale transaction log matched successfully."),
            @ApiResponse(responseCode = "404", description = "Target transaction element not found.", content = @Content)
    })

    @GetMapping("/{externalId}")
    public ResponseEntity<SaleDTOResponse> getByExternalId(@Parameter(description = "Unique identifier UUID of the transaction", required = true) @PathVariable UUID externalId) {
        return ResponseEntity.ok(saleService.getByExternalId(externalId));
    }
    @Operation(
            summary = "Get historical purchase entries by client",
            description = "Retrieves the full collection catalog of invoice logs and acquisitions linked to a specific client tracking token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client historical checkout collection retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No client found matching the provided UUID tracker.", content = @Content)
    })

    @GetMapping("/client/{clientExternalId}")
    public ResponseEntity<List<SaleDTOResponse>> getSalesByClient(@Parameter(description = "The target client profile external UUID key", required = true) @PathVariable UUID clientExternalId) {
        return ResponseEntity.ok(saleService.getSalesByClient(clientExternalId));
    }
}
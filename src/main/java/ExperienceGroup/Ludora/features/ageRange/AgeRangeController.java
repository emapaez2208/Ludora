package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTORequest;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Age Ranges", description = "Endpoints for managing age range classifications across the platform")
public class AgeRangeController {
    private final IAgeRangeService ageRangeService;

    @Operation(
            summary = "List all age ranges",
            description = "Retrieves a comprehensive list of all registered age ranges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Age ranges list retrieved successfully.")
    })
    @GetMapping
    ResponseEntity<List<AgeRangeDTOResponse>> getAll(){
        return ResponseEntity.ok(ageRangeService.getAllAgeRange());
    }

    @Operation(
            summary = "Get age range by ID",
            description = "Fetches the details of a specific age range using its external unique identifier (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Age range found successfully."),
            @ApiResponse(responseCode = "404", description = "No age range found with the given ID.", content = @Content)
    })
    @GetMapping("/{externalId}")
    ResponseEntity<AgeRangeDTOResponse> getByExternalId (@Parameter(description = "External unique ID (UUID) of the age range", required = true) @PathVariable UUID externalId){
        return ResponseEntity.ok(ageRangeService.getByExternalId(externalId));
    }

    @Operation(
            summary = "Create an age range",
            description = "Registers a new age range classification into the system after validating the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Age range created successfully.",
                    content = @Content(schema = @Schema(implementation = AgeRangeDTOResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload structure or failed validations.", content = @Content)
    })
    @PostMapping
    ResponseEntity<AgeRangeDTOResponse> create (@Valid @RequestBody AgeRangeDTORequest ageRangeDTORequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(ageRangeService.save(ageRangeDTORequest));
    }

    @Operation(
            summary = "Update an age range",
            description = "Updates the attributes of an existing age range account identified by its external UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Age range updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid update constraints provided.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Age range not found.", content = @Content)
    })
    @PutMapping("/{externalId}")
    ResponseEntity<AgeRangeDTOResponse> update(
            @Parameter(description = "External unique ID (UUID) of the age range to update", required = true) @PathVariable UUID externalId,
            @Valid @RequestBody AgeRangeDTORequest ageRangeDTORequest
    ){
        return ResponseEntity.ok(ageRangeService.update(externalId, ageRangeDTORequest));
    }

    @Operation(
            summary = "Delete an age range",
            description = "Deletes or deactivates an age range from the system using its external UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Age range deleted successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "Age range not found.", content = @Content)
    })
    @DeleteMapping("/{externalId}")
    ResponseEntity<Void> delete(@Parameter(description = "External unique ID (UUID) of the age range to delete", required = true) @PathVariable UUID externalId){
        ageRangeService.delete(externalId);
        return ResponseEntity.noContent().build();
    }

}

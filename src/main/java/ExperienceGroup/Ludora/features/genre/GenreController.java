package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "Endpoints for managing the video game categories or genres catalog")
public class GenreController {

    private final IGenreService genreService;

    @Operation(
            summary = "List all genres",
            description = "Retrieves the full list of genres. Can optionally be filtered by matching a specific name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres list retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to access this resource.", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<GenreDTO>> getAllGenre(@Parameter(description = "Optional genre name to filter the results") @RequestParam (required = false) String name){
        return ResponseEntity.ok(genreService.getAllGenre(name));
    }
    @Operation(
            summary = "Create a genre",
            description = "Registers a new video game category into the system (e.g., 'RPG', 'Action') after validating the request body input."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid data structure or validation constraints failed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to create genres.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. A genre with the same name already exists.", content = @Content)
    })

    @PostMapping
    public ResponseEntity<GenreDTO> save(@Valid @RequestBody GenreDTO genreDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.save(genreDTO));
    }

    @Operation(
            summary = "Delete a genre",
            description = "Permanently deletes or removes a genre classification from the catalog using its exact name as a query parameter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully (No Content)."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to delete genres.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No genre found matching the provided name parameter.", content = @Content)
    })
    @DeleteMapping()
    public ResponseEntity<Void> delete (@Parameter(description = "The exact name of the genre to delete", required = true) @RequestParam(required = true) String name){
        genreService.delete(name);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a genre",
            description = "Modifies the properties of an existing video game genre based on the data logs provided in the payload body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid profile update criteria provided.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to update genres.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Target genre not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. A genre with the same name already exists.", content = @Content)
    })
    @PutMapping()
    public ResponseEntity<GenreDTO> update(@Valid @RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.update(genreDTO));
    }


}

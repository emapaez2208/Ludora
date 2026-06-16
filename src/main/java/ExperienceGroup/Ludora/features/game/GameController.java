package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@AllArgsConstructor
@RequestMapping("/games")
@Tag(name = "Game Controller", description = "Endpoints for managing, filtering, and reviewing video games in Ludora")
public class GameController {
    private final IGameService gameService;

    /// --------------------------- TRAEMOS TODOS LOS JUEGOS  ( CON FILTROS ) -----------------------------
    @Operation(summary = "Get all games", description = "Returns a list of video games that match the provided optional filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Games list retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to access this resource.", content = @Content)
    })
    @GetMapping

    ResponseEntity<Page<GameDTOResponse>> getAll(
            @Parameter(description = "Page number of the list") @RequestParam int page,
            @Parameter(description = "Page size of the list") @RequestParam int size,
            @Parameter(description = "Filter by game name") @RequestParam(required = false) String name,
            @Parameter(description = "Maximum price") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Minimum price") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Minimum release date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minReleaseDate,
            @Parameter(description = "Maximum release date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxReleaseDate,
            @Parameter(description = "List of genre names") @RequestParam(required = false) List<String> genreName,
            @Parameter(description = "Range name") @RequestParam(required = false) String rangeName,
            @Parameter(description = "Developer company name") @RequestParam(required = false) String developerCompany
    ) {
        return ResponseEntity.ok(gameService.getAllGames(
                page,
                size,
                name,
                maxPrice,
                minPrice,
                minReleaseDate,
                maxReleaseDate,
                genreName,
                rangeName,
                developerCompany));
    }

    /// -------------------- TRAEMOS UN JUEGO EXTERNAL----------------------
    @Operation(summary = "Get a game by its external ID", description = "Returns the details of a single video game using its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to access this game.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No game found with the provided ID.", content = @Content)
    })
    @GetMapping("/{externalId}")
    ResponseEntity<GameDTOResponse> getByExternalId(@Parameter(description = "Unique UUID of the game", required = true) @PathVariable UUID externalId) {
        return ResponseEntity.ok(gameService.getByExternalId(externalId));
    }

    /// -------------------CREACION DE JUEGO PARA DEVELOPERS ---------------------

    @Operation(summary = "Create a new game", description = "Allows developers to register a new video game into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation error).", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Only users with CREATE_GAMES permission can perform this action.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Developer, age range, or one of the specified genres was not found.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Content unprocessable due to business logic rules.", content = @Content)
    })
    @PostMapping
    ResponseEntity<GameDTOResponse> create(@Valid @RequestBody GameDTORequest gameDTORequest) {
        return new ResponseEntity<>(gameService.save(gameDTORequest), HttpStatus.CREATED);
    }

    /// -------------------------UPDATE GAME DEVELOPER------------

    @Operation(summary = "Update an existing game", description = "Allows developers to modify a video game's data using its external ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid update data.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to update this game.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. The specified game does not belong to the authenticated developer.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Content unprocessable due to business logic rules.", content = @Content)
    })
    @PutMapping("/{externalId}")
    ResponseEntity<GameDTOResponse> update(
            @Parameter(description = "Unique UUID of the game to update", required = true) @PathVariable UUID externalId,
            @Valid @RequestBody GameDTORequest gameDTORequest
    ) {
        return ResponseEntity.ok(gameService.update(externalId, gameDTORequest));
    }

    /// ------------------AUTHORIZAR JUEGO CON ADMIN------------

    @Operation(summary = "Authorize a game", description = "Admin action to approve and authorize a video game on the platform.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully authorized."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Missing administrator permissions.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found.", content = @Content)
    })
    @PatchMapping("/{externalId}/AuthorizedGame")
    ResponseEntity<GameDTOResponse> autorizedGame(@Parameter(description = "Unique UUID of the game to authorize", required = true) @PathVariable UUID externalId) {

        return ResponseEntity.ok(gameService.authorized(externalId));
    }

    /// ------------------DESAUTHORIZAR JUEGO CON ADMIN------------

    @Operation(summary = "Deauthorize a game", description = "Admin action to revoke a video game's authorization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game successfully deauthorized."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Missing administrator permissions.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found.", content = @Content)
    })
    @DeleteMapping("/{externalId}/DesAuthorizedGame")
    ResponseEntity<Void> desautorizedGame(@Parameter(description = "Unique UUID of the game to deauthorize", required = true) @PathVariable UUID externalId){

        gameService.desauthorized(externalId);

        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "List games that need revision", description = "Returns a list of video games that currently require review by the staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revision list successfully retrieved."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Access denied.", content = @Content)
    })

    @GetMapping("/needRevision")
    ResponseEntity<List<GameDTOResponse>> getGamesNeedRevision(){
        return ResponseEntity.ok(gameService.getGamesNeedRevision());
    }

    @Operation(summary = "Request revision for a game", description = "Allows sending a specific video game to the review queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revision request successfully submitted."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You do not have permission to request revisions for this game.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict. The game is already undergoing review or presents a state conflict.", content = @Content)
    })

    @PostMapping("/askRevision/{gameExternalId}")
    ResponseEntity<GameDTOResponse> askForRevisionGame(@Parameter(description = "Unique UUID of the game requesting revision", required = true) @PathVariable UUID gameExternalId){
        return ResponseEntity.ok(gameService.askForReviewGame(gameExternalId));
    }

}

package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/games")
public class GameController {
    private final IGameService gameService;

    /// --------------------------- TRAEMOS TODOS LOS JUEGOS  ( CON FILTROS ) -----------------------------
    @GetMapping
    ResponseEntity<List<GameDTOResponse>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minReleaseDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxReleaseDate,
            @RequestParam(required = false) List<String> genreName,
            @RequestParam(required = false) String rangeName,
            @RequestParam(required = false) String developerCompany
    ) {
        return ResponseEntity.ok(gameService.getAllGames(
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
    @GetMapping("/{externalId}")
    ResponseEntity<GameDTOResponse> getByExternalId(@PathVariable UUID externalId) {
        return ResponseEntity.ok(gameService.getByExternalId(externalId));
    }

    /// -------------------CREACION DE JUEGO PARA DEVELOPERS ---------------------
    @PostMapping
    ResponseEntity<GameDTOResponse> create(@Valid @RequestBody GameDTORequest gameDTORequest) {
        return new ResponseEntity<>(gameService.save(gameDTORequest), HttpStatus.CREATED);
    }

    /// -------------------------UPDATE GAME DEVELOPER------------
    @PutMapping("/{externalId}")
    ResponseEntity<GameDTOResponse> update(
            @PathVariable UUID externalId,
            @Valid @RequestBody GameDTORequest gameDTORequest
    ) {
        return ResponseEntity.ok(gameService.update(externalId, gameDTORequest));
    }

    /// ------------------AUTHORIZAR JUEGO CON ADMIN------------

    @PatchMapping("/{externalId}/AuthorizedGame")
    ResponseEntity<GameDTOResponse> autorizedGame(@PathVariable UUID externalId){

        return ResponseEntity.ok(gameService.authorized(externalId));
    }

    /// ------------------DESAUTHORIZAR JUEGO CON ADMIN------------

    @DeleteMapping("/{externalId}/DesAuthorizedGame")
    ResponseEntity<Void> desautorizedGame(@PathVariable UUID externalId){

        gameService.desauthorized(externalId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/needRevision")
    ResponseEntity<List<GameDTOResponse>> getGamesNeedRevision(){
        return ResponseEntity.ok(gameService.getGamesNeedRevision());
    }

    @PostMapping("/askRevision/{gameExternalId}")
    ResponseEntity<GameDTOResponse> askForRevisionGame(@PathVariable UUID gameExternalId){
        return ResponseEntity.ok(gameService.askForReviewGame(gameExternalId));
    }

}

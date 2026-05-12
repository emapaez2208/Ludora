package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")

@AllArgsConstructor
public class GenreController {

    private IGenreService genreService;

    @GetMapping()
    public ResponseEntity<List<GenreDTO>> getAllGenre(@RequestParam (required = false) String name){
        return ResponseEntity.ok(genreService.getAllGenre(name));
    }

    @PostMapping
    public ResponseEntity<GenreDTO> save(@RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.save(genreDTO));
    }

    @DeleteMapping()
    public ResponseEntity<GenreDTO> delete (@RequestParam(required = true)String name){
        genreService.delete(name);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<GenreDTO> update(@RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.update(genreDTO));
    }


}

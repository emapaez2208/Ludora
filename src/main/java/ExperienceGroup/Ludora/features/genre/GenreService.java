package ExperienceGroup.Ludora.features.genre;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService  implements IGenreService{

    private final IGenreRepository iGenreRepository;


    @Override
    public List<GenreDTO> getAllGenre(String name) {
        return List.of();
    }

    @Override
    public void delete(String name) {
        

    }

    @Override
    public GenreDTO save(GenreDTO genreDTO) {
        return null;
    }
}

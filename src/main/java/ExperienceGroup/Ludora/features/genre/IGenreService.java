package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;

import java.util.List;

public interface IGenreService {

    List<GenreDTO> getAllGenre(String name);
    GenreDTO save (GenreDTO genreDTO);
    void delete (String name);
}

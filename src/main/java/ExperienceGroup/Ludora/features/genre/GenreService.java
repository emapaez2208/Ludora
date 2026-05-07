package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.Mappers.IGenreDTOMapper;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService  implements IGenreService{

    private final IGenreRepository iGenreRepository;
    private final IMapper<GenreEntity, GenreDTO> mapper;

    @Override
    public List<GenreDTO> getAllGenre(String name) {
        return List.of();
    }

    @Override
    public void delete(String name) {
        GenreEntity entity = iGenreRepository.findByName(name)
                .orElseThrow(()-> new EntityNotFoundException("No se encontro el genero"+name.toString()));

        iGenreRepository.delete(entity);
    }

    @Override
    public GenreDTO save(GenreDTO genreDTO)
    {
        GenreEntity entity = mapper.toEntity(genreDTO);
        GenreEntity entityGuardar = iGenreRepository.save(entity);
       return mapper.toDTO(entityGuardar);
    }
}

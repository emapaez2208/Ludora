package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.common.exception.GenreExistsException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;
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
        if(name == null){
           return iGenreRepository.findAll().stream()
                    .map(mapper::toDTO)
                    .toList();
        }else {
            return iGenreRepository.findAll().stream()
                    .filter(a -> a.getName().equals(name))
                    .map(mapper::toDTO)
                    .toList();
        }
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
        if(iGenreRepository.findByName(entity.getName()).isEmpty()) {

            GenreEntity entityGuardar = iGenreRepository.save(entity);
            return mapper.toDTO(entityGuardar);
        }
        throw new GenreExistsException("Genre already exists");
    }

    @Override
    public GenreDTO update(GenreDTO genreDTO) {
        GenreEntity entity = iGenreRepository.findByName(genreDTO.name())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with name = " + genreDTO.name()));

        entity.setDescription(genreDTO.description());

        GenreEntity saved = iGenreRepository.save(entity);

        return mapper.toDTO(saved);
    }
}

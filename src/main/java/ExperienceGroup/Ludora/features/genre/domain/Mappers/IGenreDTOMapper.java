package ExperienceGroup.Ludora.features.genre.domain.Mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGenreDTOMapper extends IMapper<GenreEntity, GenreDTO> {
    @Override
     GenreDTO toDTO(GenreEntity genreEntity);

    @Override
    GenreEntity toEntity(GenreDTO genreDTO);
}

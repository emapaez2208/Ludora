package ExperienceGroup.Ludora.features.genre.domain.Mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGenereRequestMapper extends IMapper<GenreEntity, GenreDTORequest> {
    @Override
     GenreDTORequest toDTO(GenreEntity genreEntity);

    @Override
    GenreEntity toEntity(GenreDTORequest genreDTORequest);
}

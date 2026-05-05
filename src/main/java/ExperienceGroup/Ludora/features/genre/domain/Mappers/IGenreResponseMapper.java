package ExperienceGroup.Ludora.features.genre.domain.Mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.genre.domain.dto.GenreDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGenreResponseMapper extends IMapper<GenreEntity, GenreDTOResponse> {

    @Override
    GenreDTOResponse toDTO(GenreEntity genreEntity) ;

    @Override
    GenreEntity toEntity(GenreDTOResponse genreDTOResponse) ;
}

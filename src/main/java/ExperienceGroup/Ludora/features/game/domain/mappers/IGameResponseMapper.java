package ExperienceGroup.Ludora.features.game.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IGameResponseMapper extends IMapper<GameEntity, GameDTOResponse> {

    @Override
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "developer", ignore = true)
    GameEntity toEntity(GameDTOResponse gameDTOResponse);

    @Override
    @Mapping(target = "ageRange", source = "ageRange.rangeName")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "mapGenres")
    @Mapping(target = "developer", source = "developer.company")
    GameDTOResponse toDTO(GameEntity gameEntity);

    @Named("mapGenres")
    default List<String> mapGenres(List<GenreEntity> genres) {
        if (genres == null) return null;
        return genres.stream()
                .map(GenreEntity::getName)
                .toList();
    }
}

package ExperienceGroup.Ludora.features.game.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGameResponseMapper extends IMapper<GameEntity, GameDTOResponse> {

    GameEntity toEntity(GameDTOResponse gameDTOResponse);
    GameDTOResponse toDTO(GameEntity gameEntity);
}

package ExperienceGroup.Ludora.features.game.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGameRequestMapper extends IMapper<GameEntity, GameDTORequest> {

    GameEntity toEntity(GameDTORequest gameDTORequest);
    GameDTORequest toDTO(GameEntity gameEntity);
    }

package ExperienceGroup.Ludora.features.game.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IGameRequestMapper extends IMapper<GameEntity, GameDTORequest> {

    @Override
    @Mapping(target = "ageRange.id", source = "ageRangeId")
    @Mapping(target = "genres", ignore = true)
    GameEntity toEntity(GameDTORequest gameDTORequest);

    @Override
    @Mapping(target = "ageRangeId", source = "ageRange.id")
    GameDTORequest toDTO(GameEntity gameEntity);
    }

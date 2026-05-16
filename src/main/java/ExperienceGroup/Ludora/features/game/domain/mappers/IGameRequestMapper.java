package ExperienceGroup.Ludora.features.game.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.GameDTORequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IGameRequestMapper extends IMapper<GameEntity, GameDTORequest> {

    @Override
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "developer", ignore = true)
    @Mapping(target = "genres", ignore = true)
    GameEntity toEntity(GameDTORequest gameDTORequest);

    @Override
    @Mapping(target = "ageRangeExternalId", source = "ageRange.externalId")
    @Mapping(target = "developerExternalId", source = "developer.externalId")
    GameDTORequest toDTO(GameEntity gameEntity);
    }

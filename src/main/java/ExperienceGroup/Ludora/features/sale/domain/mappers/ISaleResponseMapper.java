package ExperienceGroup.Ludora.features.sale.domain.mappers;

import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.domain.mappers.IClientResponseMapper;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.game.domain.dto.InfoGameDTOResponse;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;

import ExperienceGroup.Ludora.features.game.domain.mappers.IGameResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IGameResponseMapper.class, IClientResponseMapper.class})
public interface ISaleResponseMapper extends IMapper<SaleEntity, SaleDTOResponse> {

    SaleEntity toEntity(SaleDTOResponse saleDTOResponse);

    @Mapping(source = "client.userName", target = "userName")
    @Mapping(source = "games", target = "games")
    SaleDTOResponse toDTO(SaleEntity saleEntity);

    default InfoGameDTOResponse mappInfoGame(GameEntity game) {
        if (game == null) return null;
        String companyName = game.getDeveloper().getCompany();
        return new InfoGameDTOResponse(
                game.getName(),
                game.getPrice(),
                companyName
        );
    }
}

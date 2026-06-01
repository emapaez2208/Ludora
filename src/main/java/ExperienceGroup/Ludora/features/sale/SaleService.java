package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.sale.exception.SaleNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SaleService  implements ISaleService{
    private final ISaleRepository saleRepository;
    private final IMapper<SaleEntity, SaleDTOResponse> responseMapper;
    private final IMapper<SaleEntity, SaleDTORequest> requestMapper;

    private final IClientRepository clientRepository;
    private final IGameRepository gameRepository;

    @Override
    public SaleDTOResponse create(SaleDTORequest saleDTORequest) {
        ClientEntity client = clientRepository.findByExternalId(saleDTORequest.clientExternalId())
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        List<GameEntity> games = saleDTORequest.gameExternalId().stream()
                .map(gameUuid -> gameRepository.findByExternalId(gameUuid)
                        .orElseThrow(() -> new GameNotFoundException("Game not found")))
                .toList();

        SaleEntity saleEntity = requestMapper.toEntity(saleDTORequest);

        saleEntity.setClient(client);
        saleEntity.setGames(games);

        BigDecimal precio = games.stream()
                .map(GameEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        saleEntity.setTotalPrice(precio);
        return responseMapper.toDTO(saleRepository.save(saleEntity));
    }

    @Override
    public SaleDTOResponse getByExternalId(UUID externalId) {
        return saleRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found"));
    }

    @Override
    public List<SaleDTOResponse> getSalesByClient(UUID clientExternalId) {
        ClientEntity client = clientRepository.findByExternalId(clientExternalId)
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        return saleRepository.findByClient(client).stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public List<SaleDTOResponse> getAllSales(UUID externalId,
                                             LocalDateTime minDate,
                                             LocalDateTime maxDate,
                                             ESaleStatus status,
                                             BigDecimal minPrice,
                                             BigDecimal maxPrice,
                                             List<UUID> clientIds,
                                             List<UUID> gameIds) {

        PredicateSpecification<SaleEntity> spec = PredicateSpecification.allOf(
                SaleSpecification.externalIdEquals(externalId),
                SaleSpecification.dateGreaterThan(minDate),
                SaleSpecification.dateLessThan(maxDate),
                SaleSpecification.statusEquals(status),
                SaleSpecification.totalPriceGreaterThan(minPrice),
                SaleSpecification.totalPriceLessThan(maxPrice),
                SaleSpecification.clientEquals(clientIds),
                SaleSpecification.gamesEquals(gameIds)
        );

        return saleRepository.findAll(spec).stream()
                .distinct()
                .map(responseMapper::toDTO)
                .toList();
    }
}

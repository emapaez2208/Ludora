package ExperienceGroup.Ludora.features.sale;
import ExperienceGroup.Ludora.features.mercadoPago.MercadoPagoService;
import ExperienceGroup.Ludora.features.sale.domain.SaleItemEntity;
import ExperienceGroup.Ludora.features.sale.exception.SaleNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.features.cart.exception.CartEmptyException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.cart.ICartService;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ExperienceGroup.Ludora.common.utils.BusinessRules.*;

@Service
@AllArgsConstructor
public class SaleService  implements ISaleService{
    private final ISaleRepository saleRepository;
    private final IMapper<SaleEntity, SaleDTOResponse> responseMapper;
    private final IMapper<SaleEntity, SaleDTORequest> requestMapper;

    private final IClientRepository clientRepository;
    private final ICartService cartService;
    private final MercadoPagoService mercadoPago;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('BUY_GAMES') and #saleDTORequest.clientExternalId() == authentication.principal.externalId")
    public SaleDTOResponse create(SaleDTORequest saleDTORequest) {
        ClientEntity client = clientRepository.findByExternalId(saleDTORequest.clientExternalId())
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        CartEntity cart = client.getCart();
        if (cart.getGames().isEmpty()) {
            throw new CartEmptyException("Empty cart");
        }

        // se fija si tiene mas del umbral de puntos definido para aplicar el descuento.
        // si lo supera, se aplica el descuento y se le resta esa cantidad de puntos. se guarda en el cliente esta info

        boolean hasDiscount = client.getPoints() >= POINTS_THRESHOLD;
        if (hasDiscount) {
            client.setPoints(client.getPoints() - POINTS_THRESHOLD);
            clientRepository.save(client);
        }

        SaleEntity saleEntity = requestMapper.toEntity(saleDTORequest);
        saleEntity.setClient(client);
        saleEntity.setHasDiscount(hasDiscount);

        // se crea una lista con los items de la venta según lo que estaba en el carrito.
        // cada ítem guarda la venta, el juego y el precio del momento de la venta

        List<SaleItemEntity> items = cart.getGames().stream()
                .map(game -> {
                    SaleItemEntity item = new SaleItemEntity();
                    item.setSale(saleEntity);
                    item.setGame(game);

                    BigDecimal price = hasDiscount
                            ? game.getPrice().multiply(BigDecimal.ONE.subtract(DISCOUNT_PERCENTAGE))
                                  .setScale(2, RoundingMode.HALF_UP)
                            : game.getPrice().setScale(2, RoundingMode.HALF_UP);

                    item.setPriceAtSale(price);
                    return item;
                })
                .toList();

        // se calcula el total según los precios que figuran en los ítems de la venta

        BigDecimal totalPrice = items.stream()
                .map(SaleItemEntity::getPriceAtSale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        saleEntity.setItems(items);
        saleEntity.setTotalPrice(totalPrice);

        // cálculo de recompensa de puntos por compra.
        // los puntos ganados se le suman al cliente.

        Integer earnedPoints = calculateEarnedPoints(totalPrice);
        saleEntity.setEarnedPoints(earnedPoints);

        client.setPoints(client.getPoints() + earnedPoints);
        clientRepository.save(client);

        SaleEntity saved = saleRepository.save(saleEntity);

        cartService.clearCart(client.getExternalId());

        return responseMapper.toDTO(saved);
    }

    @PreAuthorize("hasAuthority('BUY_GAMES')")
    public String paySaleMP(UUID externalId){
        SaleEntity sale = saleRepository.findByExternalId(externalId)
                .orElseThrow(SaleNotFoundException::new);

        cartService.clearCart(sale.getClient().getExternalId());
        return mercadoPago.createPay(sale.getItems(), sale.getExternalId());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public SaleDTOResponse getByExternalId(UUID externalId) {
        return saleRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found"));
    }

    @Override
    @PreAuthorize("#clientExternalId == authentication.principal.externalId")
    public List<SaleDTOResponse> getSalesByClient(UUID clientExternalId) {
        ClientEntity client = clientRepository.findByExternalId(clientExternalId)
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        return saleRepository.findByClient(client).stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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

    // CÁLCULO DE PUNTOS

    public Integer calculateEarnedPoints(BigDecimal totalPrice){

        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        return totalPrice.multiply(REWARD_POINTS_PERCENTAGE)
                .setScale(0, RoundingMode.FLOOR)
                .intValue();

    }

}

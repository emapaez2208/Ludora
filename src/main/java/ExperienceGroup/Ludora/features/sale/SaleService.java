package ExperienceGroup.Ludora.features.sale;
import ExperienceGroup.Ludora.features.mercadoPago.MercadoPagoService;
import ExperienceGroup.Ludora.features.sale.exception.SaleNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.common.exception.CartEmptyException;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SaleService  implements ISaleService{
    private final ISaleRepository saleRepository;
    private final IMapper<SaleEntity, SaleDTOResponse> responseMapper;
    private final IMapper<SaleEntity, SaleDTORequest> requestMapper;

    private final IClientRepository clientRepository;
    private final ICartService cartService;

    private static final BigDecimal REWARD_POINTS_PERCENTAGE = BigDecimal.valueOf(0.08);
    private static final Integer POINTS_THRESHOLD = 10000;
    private static final BigDecimal MAX_PRICE_FOR_DISCOUNT = BigDecimal.valueOf(6000);
    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(0.10);
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

        BigDecimal totalPrice = BigDecimal.valueOf(cart.getTotalPrice());

        SaleEntity saleEntity = requestMapper.toEntity(saleDTORequest);

        saleEntity.setClient(client);
        saleEntity.setGames(new ArrayList<>(cart.getGames()));
        saleEntity.setTotalPrice(totalPrice);

        Integer earnedPoints = calculateEarnedPoints(totalPrice);

        saleEntity.setEarnedPoints(earnedPoints);

        SaleEntity saved = saleRepository.save(saleEntity);

        cartService.clearCart(client.getExternalId());

        return responseMapper.toDTO(saved);
    }

    @PreAuthorize("hasAuthority('BUY_GAMES')")
    public String paySaleMP(UUID externalId){
        SaleEntity sale = saleRepository.findByExternalId(externalId)
                .orElseThrow(SaleNotFoundException::new);

        cartService.clearCart(sale.getClient().getExternalId());
        return mercadoPago.createPay(sale.getGames(), sale.getExternalId());
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

        Integer earnedPoints = totalPrice.multiply(REWARD_POINTS_PERCENTAGE).intValue();

        return earnedPoints;
    }

}

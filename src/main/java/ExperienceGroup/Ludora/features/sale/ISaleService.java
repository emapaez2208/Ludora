package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ISaleService {

    SaleDTOResponse create(SaleDTORequest saleDTORequest);

    SaleDTOResponse getByExternalId(UUID externalId);

    Page<SaleDTOResponse> getSalesByClient(int page, int size, UUID clientExternalId);

    Page<SaleDTOResponse> getAllSales(int page,
                                      int size,
                                      UUID externalId,
                                      LocalDateTime minDate,
                                      LocalDateTime maxDate,
                                      ESaleStatus status,
                                      BigDecimal minPrice,
                                      BigDecimal maxPrice,
                                      List<UUID> clientIds,
                                      List<UUID> gameIds);

    String paySaleMP(UUID externalId);
}

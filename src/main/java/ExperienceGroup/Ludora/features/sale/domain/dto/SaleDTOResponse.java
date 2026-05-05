package ExperienceGroup.Ludora.features.sale.domain.dto;

import ExperienceGroup.Ludora.features.sale.ESaleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDTOResponse (LocalDateTime date,
                                ESaleStatus status,
                                double totalPrice,
                                Long clientId,
                                List<Long> gameIds
){
}

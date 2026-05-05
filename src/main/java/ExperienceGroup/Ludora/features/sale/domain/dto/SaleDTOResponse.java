package ExperienceGroup.Ludora.features.sale.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDTOResponse (LocalDateTime date,
                                String status,
                                double totalPrice,
                                Long clientId,
                                List<Long> gameIds
){
}

package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;

import java.util.List;
import java.util.UUID;

public interface ISaleService {

    SaleDTOResponse create(SaleDTORequest saleDTORequest);

    SaleDTOResponse getByExternalId(UUID externalId);

    List<SaleDTOResponse> getSalesByClient(UUID clientExternalId);

}

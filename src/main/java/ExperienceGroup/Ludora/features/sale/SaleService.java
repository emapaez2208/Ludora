package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.common.exception.SaleNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SaleService  implements ISaleService{
    private final ISaleRepository saleRepository;
    private final IMapper<SaleEntity, SaleDTOResponse> responseMapper;
    private final IMapper<SaleEntity, SaleDTORequest> requestMapper;

    //private final servicio cliente
    //private final servicio juego

    @Override
    public SaleDTOResponse create(SaleDTORequest saleDTORequest) {
        return null;
    }

    @Override
    public SaleDTOResponse getByExternalId(UUID externalId) {
        return saleRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found"));
    }

    @Override
    public List<SaleDTOResponse> getSalesByClient(UUID clientExternalId) {
        return saleRepository.findByClientExternalId(clientExternalId).stream()
                .map(responseMapper::toDTO)
                .toList();
    }
}

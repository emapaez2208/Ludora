package ExperienceGroup.Ludora.features.mercadoPago;

import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.sale.ISaleRepository;
import ExperienceGroup.Ludora.features.sale.domain.dto.SaleDTORequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private final ISaleRepository saleRepository;
    private final IGameRepository gameRepository;

    @Transactional
    public String crearPago(List<GameEntity> games) throws MPException, MPApiException {

        List<PreferenceItemRequest> items = new ArrayList<>();

        for(GameEntity game : games){
            items.add(PreferenceItemRequest.builder()
                            .title(game.getName())
                            .quantity(1)
                            .unitPrice(game.getPrice())
                            .currencyId("ARS")
                            .build());
        }

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success("http://localhost:8080/pay/success")
                                .failure("http://localhost:8080/pay/failure")
                                .pending("http://localhost:8080/pay/pending")
                                .build()
                )
                .autoReturn("aproved")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }
}

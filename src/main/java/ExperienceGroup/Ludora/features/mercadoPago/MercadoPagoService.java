package ExperienceGroup.Ludora.features.mercadoPago;

import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.mercadoPago.exception.MercadoPagoFailedException;
import ExperienceGroup.Ludora.features.sale.ISaleRepository;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private final ISaleRepository saleRepository;
    private final IGameRepository gameRepository;

    @Value("${app.base.url}")
    private String baseUrl;


    @Transactional
    public String createPay(List<GameEntity> games, UUID saleId) {

        try {
            List<PreferenceItemRequest> items = new ArrayList<>();

            for (GameEntity game : games) {
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
                            PreferenceBackUrlsRequest.builder()     // mercado pago redirigue al usuario a estas url para mostrarles nuestro msj correspondiente
                                    .success(baseUrl + "/pay/success")
                                    .failure(baseUrl + "/pay/failure")
                                    .pending(baseUrl + "/pay/pending")
                                    .build()
                    )
                    .autoReturn("aproved")
                    .notificationUrl(baseUrl + "/pay/webhook") // mercado pago envia notificacion de estado de pago a esta url
                    .externalReference(saleId.toString())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();

        } catch (MPApiException | MPException ex) {
            throw new MercadoPagoFailedException();
        }
    }
}

package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.mercadoPago.exception.MercadoPagoFailedException;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;
import ExperienceGroup.Ludora.features.sale.ISaleRepository;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.exception.SaleNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final ISaleRepository saleRepository;
    private final IClientRepository clientRepository;

    @Transactional
    public void processWebhook(String type, String paymentId) {

        try {
            if ("payment".equals(type)) { // verifica que el webhook recibido sea del tipo deseado
                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.valueOf(paymentId));

                String externalRefence = payment.getExternalReference();
                if(externalRefence == null){
                    System.out.println("Received webhook does not contain external reference");
                    throw new MercadoPagoFailedException();
                }

                SaleEntity sale = saleRepository.findByExternalId(UUID.fromString(externalRefence))
                        .orElseThrow(SaleNotFoundException::new);

                if(sale.getStatus() == ESaleStatus.APPROVED){
                    return; // Si la venta ya fue marcada como aprovada previamente, no quiero que me vuelva a modificar dicha venta
                }           // mercado pago puede reenviar los webhook, lo que podria generar juegos duplicados en la lista de los juegos del cliente

                ClientEntity client = clientRepository.findByExternalId(sale.getClient().getExternalId())
                        .orElseThrow(UserNotFoundException::new);

                switch (payment.getStatus()) {

                    case "approved":
                            List<GameEntity> games = client.getGames();
                            sale.setStatus(ESaleStatus.APPROVED);
                            games.addAll(sale.getGames());
                            client.setGames(games);
                            clientRepository.save(client);
                        break;

                    case "pending":
                        sale.setStatus(ESaleStatus.PENDING);
                        break;

                    default:
                        sale.setStatus(ESaleStatus.REJECTED);
                        break;
                }

                saleRepository.save(sale);
            }
        } catch (MPApiException | MPException ex) {
            throw new MercadoPagoFailedException();
        }
    }
}

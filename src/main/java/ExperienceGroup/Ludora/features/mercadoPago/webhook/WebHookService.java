package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import ExperienceGroup.Ludora.features.sale.ISaleRepository;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.exception.SaleNotFoundException;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebHookService {

    private final ISaleRepository saleRepository;

    public void getWebhook(String type, String paymentId) throws MPException, MPApiException {

        if(type.equals("payment")) { // verifica que el webhook recibido sea del tipo deseado
            PaymentClient paymentClient = new PaymentClient();

            Payment payment = paymentClient.get(Long.valueOf(paymentId));

            SaleEntity sale = saleRepository.findByExternalId(UUID.fromString(payment.getExternalReference()))
                    .orElseThrow(SaleNotFoundException::new);


        }
    }
}

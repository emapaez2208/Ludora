package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void getWebhook(
            @RequestParam(required = false) String type,
            @RequestParam(name = "data.id", required = false) String paymentId){

        webhookService.processWebhook(type, paymentId);
    }

}

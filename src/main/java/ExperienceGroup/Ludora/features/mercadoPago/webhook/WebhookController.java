package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pays/webhook")
public class WebhookController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void getWebhook(
            @RequestParam(required = false) String type,
            @RequestParam(name = "data.id", required = false) String paymentId){

    }

}

package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<Void> getWebhook(
            @RequestParam(required = false) String type,
            @RequestParam(name = "data.id", required = false) String paymentId){

        webhookService.processWebhook(type, paymentId);

        return ResponseEntity.ok().build();
    }

}

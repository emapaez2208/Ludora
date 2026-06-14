package ExperienceGroup.Ludora.features.mercadoPago.webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay/webhook")
@Tag(name = "Webhooks (Mercado Pago)", description = "Asynchronous server-to-server listener endpoint to capture instant event triggers from Mercado Pago platform")
public class WebhookController {

    private final WebhookService webhookService;

    @Operation(
            summary = "Receive Webhook notification trigger",
            description = "Processes backend Instant Payment Notifications (IPN) dispatched asynchronously by Mercado Pago whenever a transaction state switches (e.g., payment approved)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification received, validated, and processed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload properties or notification signature mismatch error.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> getWebhook(
            @Parameter(description = "The specific topic event type emitted (e.g., 'payment', 'chargeback')") @RequestParam(required = false) String type,
            @Parameter(description = "The unique platform processing resource ID parameter reference (?data.id=...)") @RequestParam(name = "data.id", required = false) String paymentId){

        webhookService.processWebhook(type, paymentId);

        return ResponseEntity.ok().build();
    }

}

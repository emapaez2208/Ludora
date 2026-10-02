package ExperienceGroup.Ludora.features.mercadoPago;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
@Tag(name = "Payments (Mercado Pago)", description = "Redirection endpoints (Callbacks) handling landing actions after gateway transaction processing")
public class MercadoPagoController {

    @Operation(
            summary = "Success checkout callback response handler",
            description = "Landing endpoint destination where Mercado Pago routes the client context after a successful transaction approval."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice settlement confirmed notification message.")
    })

    @GetMapping("/success")
    public ResponseEntity<String> success (){
        return ResponseEntity.ok("Payment approved successfully");
    }

    @Operation(
            summary = "Failure checkout callback response handler",
            description = "Landing endpoint destination where Mercado Pago routes the client context if the transaction is declined, aborted, or canceled."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice process declined/canceled notification message.")
    })
    @GetMapping("/failure")
    public ResponseEntity<String> failure () {
        return ResponseEntity.ok("The payment process was canceled or declined");
    }

    @Operation(
            summary = "Pending checkout callback response handler",
            description = "Landing endpoint destination where Mercado Pago routes the client context when payment resolution is under review or waiting for async clearance."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice transaction evaluation state pending notification message.")
    })

    @GetMapping("/pending")
    public ResponseEntity<String> pending () {
        return ResponseEntity.ok("Your payment is currently pending or under review");
    }
}

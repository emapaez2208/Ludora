package ExperienceGroup.Ludora.features.mercadoPago;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class MercadoPagoController {


    @GetMapping("/success")
    public ResponseEntity<String> success (){
        return ResponseEntity.ok("Payment approved successfully");
    }

    @GetMapping("/failure")
    public ResponseEntity<String> failure () {
        return ResponseEntity.ok("The payment process was canceled or declined");
    }

    @GetMapping("/pending")
    public ResponseEntity<String> pending () {
        return ResponseEntity.ok("Your payment is currently pending or under review");
    }
}

package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/Cart")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{clientId}")
    ResponseEntity<CartDTOResponse> getCartByClient(@PathVariable UUID clientExternalId){

        return ResponseEntity.ok( cartService.getCartByClient(clientExternalId));
    }

    @PostMapping("/{clientId}/games/{gameId}")
    public ResponseEntity<CartDTOResponse> addGame(
            @PathVariable UUID clientId,
            @PathVariable UUID gameId) {

        return ResponseEntity.ok(
                cartService.addGame(clientId, gameId)
        );
    }

    @DeleteMapping("/{clientId}/games/{gameId}")
    public ResponseEntity<CartDTOResponse> removeGame(
            @PathVariable UUID clientId,
            @PathVariable UUID gameId) {

        return ResponseEntity.ok(
                cartService.removeGame(clientId, gameId)
        );
    }

    @DeleteMapping("/{clientId}/clear")
    public ResponseEntity<Void> clearCart(
            @PathVariable UUID clientId) {

        cartService.clearCart(clientId);

        return ResponseEntity.noContent().build();
    }



}

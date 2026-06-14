package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.dto.CartDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/clients/cart")
@Tag(name = "Shopping Cart", description = "Endpoints for managing client shopping carts (adding, removing, and clearing games)")
public class CartController {

    private final ICartService cartService;

    @Operation(
            summary = "Get client cart by ID",
            description = "Retrieves the shopping cart details belonging to a specific client identified by their unique UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Client or cart not found.", content = @Content)
    })

    @GetMapping("/{clientId}")
    ResponseEntity<CartDTOResponse> getCartByClient(@Parameter(description = "The unique UUID of the client", required = true) @PathVariable UUID clientId){

        return ResponseEntity.ok( cartService.getCartByClient(clientId));
    }

    @Operation(
            summary = "Get current user cart",
            description = "Retrieves the shopping cart details of the currently authenticated client session."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user's cart retrieved successfully.")
    })
    @GetMapping
    ResponseEntity<CartDTOResponse> getMyCart(){
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @Operation(
            summary = "Add a game to the cart",
            description = "Adds a specific game into a client's shopping cart and returns the updated state of the cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully added. Returns the updated cart items."),
            @ApiResponse(responseCode = "404", description = "Specified client or game not found.", content = @Content)
    })
    @PostMapping("/{clientId}/games/{gameId}")
    public ResponseEntity<CartDTOResponse> addGame(
            @Parameter(description = "The unique UUID of the client", required = true) @PathVariable UUID clientId,
            @Parameter(description = "The unique UUID of the game to add", required = true) @PathVariable UUID gameId) {

        return ResponseEntity.ok(
                cartService.addGame(clientId, gameId)
        );
    }
    @Operation(
            summary = "Remove a game from the cart",
            description = "Removes a specific game from a client's shopping cart and returns the resulting state of the cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully removed. Returns the updated cart items."),
            @ApiResponse(responseCode = "404", description = "Client, cart, or game not found.", content = @Content)
    })

    @DeleteMapping("/{clientId}/games/{gameId}")
    public ResponseEntity<CartDTOResponse> removeGame(
            @Parameter(description = "The unique UUID of the client", required = true) @PathVariable UUID clientId,
            @Parameter(description = "The unique UUID of the game to remove", required = true) @PathVariable UUID gameId) {

        return ResponseEntity.ok(
                cartService.removeGame(clientId, gameId)
        );
    }

    @Operation(
            summary = "Clear shopping cart",
            description = "Removes all games and items contained inside the specified client's shopping cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Shopping cart cleared successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "Client or cart not found.", content = @Content)
    })

    @DeleteMapping("/{clientId}/clear")
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "The unique UUID of the client whose cart will be cleared", required = true) @PathVariable UUID clientId) {

        cartService.clearCart(clientId);

        return ResponseEntity.noContent().build();
    }



}

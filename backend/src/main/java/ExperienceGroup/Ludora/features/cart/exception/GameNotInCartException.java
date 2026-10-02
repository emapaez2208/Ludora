package ExperienceGroup.Ludora.features.cart.exception;

public class GameNotInCartException extends RuntimeException {
    public GameNotInCartException(String message) {
        super(message);
    }
    public GameNotInCartException() {
        super("The game was not found in the cart");
    }

}

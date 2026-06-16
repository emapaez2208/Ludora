package ExperienceGroup.Ludora.features.cart.exception;

public class GameAlreadyInCartException extends RuntimeException {
    public GameAlreadyInCartException(String message) {
        super(message);
    }
    public GameAlreadyInCartException() {
        super("The game is already in the cart");
    }

}

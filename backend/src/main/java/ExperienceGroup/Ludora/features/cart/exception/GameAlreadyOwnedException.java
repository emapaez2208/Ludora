package ExperienceGroup.Ludora.features.cart.exception;

public class GameAlreadyOwnedException extends RuntimeException {
    public GameAlreadyOwnedException(String message) {
        super(message);
    }
    public GameAlreadyOwnedException() {
        super("You already own this game in your library");
    }

}

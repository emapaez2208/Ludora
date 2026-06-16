package ExperienceGroup.Ludora.features.review.exception;

public class GameNotPurchasedException extends RuntimeException {
    public GameNotPurchasedException(String message) {
        super(message);
    }
    public GameNotPurchasedException() {
        super("Game not purchased");
    }

}

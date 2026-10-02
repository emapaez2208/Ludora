package ExperienceGroup.Ludora.features.game.exception;

public class GameIsNotFromTheDevException extends RuntimeException {
    public GameIsNotFromTheDevException(String message) {
        super(message);
    }
    public GameIsNotFromTheDevException(){
        super("Game is not from the developer");
    }
}

package ExperienceGroup.Ludora.features.game.exception;

import jakarta.persistence.EntityNotFoundException;

public class GameNotFoundException extends EntityNotFoundException {
    public GameNotFoundException(String message) {
        super(message);
    }
    public GameNotFoundException(){
        super("Game not found");
    }
}

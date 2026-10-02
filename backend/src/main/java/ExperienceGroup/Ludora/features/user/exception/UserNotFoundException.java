package ExperienceGroup.Ludora.features.user.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(){
        super("User not found");
    }
}

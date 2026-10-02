package ExperienceGroup.Ludora.features.user.exception;

public class UserExistsWithEmailException extends RuntimeException{
    public UserExistsWithEmailException(String message) {
        super(message);
    }
}

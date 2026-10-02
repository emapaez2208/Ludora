package ExperienceGroup.Ludora.features.user.exception;

public class UserExistsWithUsernameException extends RuntimeException {
    public UserExistsWithUsernameException(String message) {
        super(message);
    }
}

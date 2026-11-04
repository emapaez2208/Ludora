package ExperienceGroup.Ludora.features.user.exception;

public class IlegalPasswordChangeException extends IllegalArgumentException {
    public IlegalPasswordChangeException(String message) {
        super(message);
    }
}

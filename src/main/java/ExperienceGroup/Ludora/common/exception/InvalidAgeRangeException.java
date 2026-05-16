package ExperienceGroup.Ludora.common.exception;

public class InvalidAgeRangeException extends RuntimeException {
    public InvalidAgeRangeException(String message) {super(message);}
    public InvalidAgeRangeException(){
        super("Age range not valid");
    }
}

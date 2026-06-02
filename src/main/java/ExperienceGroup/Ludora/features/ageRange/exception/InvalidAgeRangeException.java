package ExperienceGroup.Ludora.features.ageRange.exception;

public class InvalidAgeRangeException extends RuntimeException {
    public InvalidAgeRangeException(String message) {super(message);}
    public InvalidAgeRangeException(){
        super("Age range not valid");
    }
}

package ExperienceGroup.Ludora.common.exception;

public class IllegalEmailException extends IllegalArgumentException{
    public IllegalEmailException() {
        super("Invalid Email format");
    }
    public IllegalEmailException(String message){
        super(message);
    }
}

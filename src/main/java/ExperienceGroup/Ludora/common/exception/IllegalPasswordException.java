package ExperienceGroup.Ludora.common.exception;

public class IllegalPasswordException extends IllegalArgumentException{
    public IllegalPasswordException(){
        super("invalid password format");
    }

    public IllegalPasswordException(String message){
        super(message);
    }
}

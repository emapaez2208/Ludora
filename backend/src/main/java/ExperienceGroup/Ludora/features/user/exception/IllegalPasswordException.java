package ExperienceGroup.Ludora.features.user.exception;

public class IllegalPasswordException extends IllegalArgumentException{
    public IllegalPasswordException(){
        super("invalid password format");
    }

    public IllegalPasswordException(String message){
        super(message);
    }
}

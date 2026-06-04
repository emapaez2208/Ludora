package ExperienceGroup.Ludora.common.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String message) {
        super(message);
    }
    public CartEmptyException(){
        super("The cart is empty");
    }
}

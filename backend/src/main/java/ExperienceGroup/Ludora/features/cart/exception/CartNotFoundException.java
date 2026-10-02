package ExperienceGroup.Ludora.features.cart.exception;

import jakarta.persistence.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {
    public CartNotFoundException(String message) {
        super(message);
    }
    public CartNotFoundException() {
        super("Cart not found");
    }

}

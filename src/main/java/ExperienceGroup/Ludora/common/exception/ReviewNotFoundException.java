package ExperienceGroup.Ludora.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
    public ReviewNotFoundException() {super("Review not found");}
}

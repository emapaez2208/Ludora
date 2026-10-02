package ExperienceGroup.Ludora.features.review.exception;

import jakarta.persistence.EntityNotFoundException;

public class ReviewNotFoundException extends EntityNotFoundException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
    public ReviewNotFoundException() {super("Review not found");}
}

package ExperienceGroup.Ludora.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class AgeRangeNotFoundException extends EntityNotFoundException {
    public AgeRangeNotFoundException(String message) {
        super(message);
    }
    public AgeRangeNotFoundException(){
        super("Age range not found");
    }
}

package ExperienceGroup.Ludora.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class SaleNotFoundException extends EntityNotFoundException {
    public SaleNotFoundException(String message) {
        super(message);
    }
    public SaleNotFoundException(){
        super("Sale not found");
    }
}

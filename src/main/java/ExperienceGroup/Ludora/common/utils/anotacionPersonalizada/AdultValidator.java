package ExperienceGroup.Ludora.common.utils.anotacionPersonalizada;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return true;
        }

        return Period.between(localDate, localDate.now()).getYears() >= 18;
    }
}

package ExperienceGroup.Ludora.common.utils.anotacionPersonalizada;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
@Documented
public @interface Adult {
    String message() default "Must be over 18 years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
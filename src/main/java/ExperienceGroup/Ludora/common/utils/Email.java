package ExperienceGroup.Ludora.common.utils;

import ExperienceGroup.Ludora.common.exception.IllegalEmailException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\\.)+[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Email(String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalEmailException("Invalid email format: " + value);
        }
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}

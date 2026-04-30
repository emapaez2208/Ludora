package ExperienceGroup.Ludora.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Password(String value) {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$");

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Password(String value){
        if(value == null || !PASSWORD_PATTERN.matcher(value).matches()){
            throw new IllegalArgumentException("Invalid Password format: " + value);
        }
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}

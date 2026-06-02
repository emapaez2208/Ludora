package ExperienceGroup.Ludora.auth.credentials.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class CredentialsNotFoundException extends EntityNotFoundException {
    public CredentialsNotFoundException(String message) {
        super(message);
    }
}

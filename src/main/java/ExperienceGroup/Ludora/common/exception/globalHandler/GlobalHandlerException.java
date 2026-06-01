package ExperienceGroup.Ludora.common.exception.globalHandler;

import ExperienceGroup.Ludora.common.exception.dto.ErrorResponseDTO;
import ExperienceGroup.Ludora.features.ageRange.exception.AgeRangeNotFoundException;
import ExperienceGroup.Ludora.features.ageRange.exception.InvalidAgeRangeException;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.review.exception.ReviewNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.IllegalEmailException;
import ExperienceGroup.Ludora.features.user.exception.IllegalPasswordException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(IllegalEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handlerIllegalEmail(IllegalEmailException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handlerIllegalPassword(IllegalPasswordException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerUserNotFound(UserNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerReviewNotFound(ReviewNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerGameNotFound(GameNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AgeRangeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerAgeRangeNotFound(AgeRangeNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerEntityNotFound(EntityNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidAgeRangeException.class)
    public ResponseEntity<ErrorResponseDTO> handlerInvalidAgeRange(InvalidAgeRangeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpected(Exception ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex){
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message){
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(error, status);
    }

}
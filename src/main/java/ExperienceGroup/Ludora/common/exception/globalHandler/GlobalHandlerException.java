package ExperienceGroup.Ludora.common.exception.globalHandler;

import ExperienceGroup.Ludora.features.cart.exception.*;
import ExperienceGroup.Ludora.features.user.exception.PasswordInvalidException;
import ExperienceGroup.Ludora.features.genre.exception.GenreExistsException;
import ExperienceGroup.Ludora.common.exception.dto.ErrorResponseDTO;
import ExperienceGroup.Ludora.features.ageRange.exception.AgeRangeNotFoundException;
import ExperienceGroup.Ludora.features.ageRange.exception.InvalidAgeRangeException;
import ExperienceGroup.Ludora.features.game.exception.GameNotFoundException;
import ExperienceGroup.Ludora.features.mercadoPago.exception.MercadoPagoFailedException;
import ExperienceGroup.Ludora.features.review.exception.ReviewNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.IllegalEmailException;
import ExperienceGroup.Ludora.features.user.exception.IllegalPasswordException;
import ExperienceGroup.Ludora.features.user.exception.UserNotFoundException;
import ExperienceGroup.Ludora.features.user.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.LockedException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(IllegalEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handlerIllegalEmail(IllegalEmailException ex){
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(IllegalPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handlerIllegalPassword(IllegalPasswordException ex){
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
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
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpected(Exception ex){
        log.error("Unexpected error", ex);  /// msj para saber que esta pasando
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server. We don't know what happened. We're sorry.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex){
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(UserExistsWithEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handlerUserExistsEmail(UserExistsWithEmailException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserExistsWithUsernameException.class)
    public ResponseEntity<ErrorResponseDTO> handlerUserExistsUsername(UserExistsWithUsernameException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponseDTO> handlerLockedAccount(LockedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Your account is Locked");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseDTO> handlerDisabledAccount(DisabledException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Your account is disabled");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action.");
    }

    @ExceptionHandler(GenreExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handlerGenreExistsException (GenreExistsException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorResponseDTO> handlerCartEmpty(CartEmptyException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(GameAlreadyInCartException.class)
    public ResponseEntity<ErrorResponseDTO> handlerGameAlreadyInCart(GameAlreadyInCartException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerCartNotFound(CartNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(GameNotInCartException.class)
    public ResponseEntity<ErrorResponseDTO> handlerGameNotInCart(GameNotInCartException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(GameAlreadyOwnedException.class)
    public ResponseEntity<ErrorResponseDTO> handlerGameAlreadyOwned(GameAlreadyOwnedException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MercadoPagoFailedException.class)
        public ResponseEntity<ErrorResponseDTO> handlerMercadoPagoFailedException ( MercadoPagoFailedException ex){
            return buildResponse(HttpStatus.BAD_GATEWAY , ex.getMessage());
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handlerPasswordInvalid(PasswordInvalidException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /// ---------------------- MSJ ERROR CONSTRUCTOR ----------------------------------- ///
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
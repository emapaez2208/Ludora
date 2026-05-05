package ExperienceGroup.Ludora.common.exception.globalHandler;

import ExperienceGroup.Ludora.common.exception.IllegalEmailException;
import ExperienceGroup.Ludora.common.exception.IllegalPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(IllegalEmailException.class)
    public ResponseEntity<String> HandlerIllegalEmail(IllegalEmailException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalPasswordException.class)
    public ResponseEntity<String> HandlerIllegalPassword(IllegalPasswordException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}

package app.invoice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<Object> handleNullValueException(){
        return new ResponseEntity<Object>("Niepoprawne hasło!", HttpStatus.NOT_FOUND);
    }
}

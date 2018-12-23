package co.enoobong.authenticationwizard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailTakenException extends RuntimeException {

    public EmailTakenException(String email) {
        super(String.format("%s has already been taken", email));
    }
}

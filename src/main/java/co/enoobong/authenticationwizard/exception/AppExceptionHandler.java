package co.enoobong.authenticationwizard.exception;

import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@ControllerAdvice
public class AppExceptionHandler {

    private static final Logger L = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<MessageResponse> handleException(Exception exception) {
        L.error("An Error Occurred", exception);

        if (exception instanceof MethodArgumentNotValidException) {
            final StringBuilder errorMessageBuilder = new StringBuilder();
            ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().forEach(fieldError -> errorMessageBuilder.append(fieldError.getField())
                    .append(" ")
                    .append(fieldError.getDefaultMessage())
                    .append(System.lineSeparator()));

            final MessageResponse errorResponse = new MessageResponse(errorMessageBuilder.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            final HttpStatus httpStatus = resolveAnnotatedResponseStatus(exception);
            final MessageResponse errorResponse = new MessageResponse(exception.getMessage());
            return new ResponseEntity<>(errorResponse, httpStatus);
        }

    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
        final ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            if (exception instanceof HttpMessageNotReadableException || exception instanceof IllegalArgumentException) {
                return HttpStatus.BAD_REQUEST;
            } else if (exception instanceof AuthenticationException) {
                return HttpStatus.UNAUTHORIZED;
            } else {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}


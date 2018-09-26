package com.lucasluc4.temptunes.exceptionhandler;

import com.lucasluc4.temptunes.exception.CouldNotRetrievePlaylistException;
import com.lucasluc4.temptunes.exception.NotFoundException;
import com.lucasluc4.temptunes.exception.TempTunesError;
import com.lucasluc4.temptunes.exception.ValidationException;
import com.lucasluc4.temptunes.response.ErrorDetails;
import com.lucasluc4.temptunes.response.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class TempTunesExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempTunesExceptionHandler.class);

    private static final String unknownError = "An unknown internal error occurred";

    @ExceptionHandler(CouldNotRetrievePlaylistException.class)
    public final ResponseEntity<RestResponse> handleCouldNotRetrievePlaylist (CouldNotRetrievePlaylistException ex, WebRequest request) {

        LOGGER.error("handleCouldNotRetrievePlaylist: ", ex);

        ErrorDetails errorDetails = new ErrorDetails(ex.getErrorTitle(), ex.getMessage());

        RestResponse restResponse = new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails, null);

        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<RestResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {

        LOGGER.error("handleNotFoundException: ", ex);

        ErrorDetails errorDetails = new ErrorDetails(ex.getErrorTitle(), ex.getMessage());

        RestResponse restResponse = new RestResponse(HttpStatus.NOT_FOUND.value(), errorDetails, null);

        return new ResponseEntity<>(restResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<RestResponse> handleValidationException(ValidationException ex, WebRequest request) {

        LOGGER.error("handleValidationException: ", ex);

        ErrorDetails errorDetails = new ErrorDetails(ex.getErrorTitle(), ex.getMessage());

        RestResponse restResponse = new RestResponse(HttpStatus.BAD_REQUEST.value(), errorDetails, null);

        return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<RestResponse> handleGenericException(Exception ex, WebRequest request) {

        LOGGER.error("handleGenericException: ", ex);

        ErrorDetails errorDetails = new ErrorDetails(TempTunesError.UNKNOWN.getTitle(), unknownError);

        RestResponse restResponse = new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails, null);

        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

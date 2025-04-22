package com.PoseidonCapitalSolutions.TradingApp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

/**
 * Global exception handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle resource not found exception string.
     *
     * @param ex    the ex
     * @param model the model
     * @return the string
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.NOT_FOUND.toString());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }

    /**
     * Handle last admin exception string.
     *
     * @param ex    the ex
     * @param model the model
     * @return the string
     */
    @ExceptionHandler(LastAdminException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLastAdminException(LastAdminException ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST.toString());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }

    /**
     * Handle access denied exception string.
     *
     * @param ex    the ex
     * @param model the model
     * @return the string
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.FORBIDDEN.toString());
        model.addAttribute("errorMessage", "Access Denied");
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }

    /**
     * Handle generic exception string.
     *
     * @param ex    the ex
     * @param model the model
     * @return the string
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }
}

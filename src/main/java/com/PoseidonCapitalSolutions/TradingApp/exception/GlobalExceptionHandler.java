package com.PoseidonCapitalSolutions.TradingApp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.NOT_FOUND.toString());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }

    @ExceptionHandler(LastAdminException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLastAdminException(LastAdminException ex, Model model) {
        logger.error(ex.getMessage());

        model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST.toString());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTimestamp", Instant.now());

        return "error";
    }

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
